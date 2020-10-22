package com.pulse.core.utils

import android.util.Log.INFO
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okhttp3.internal.platform.Platform
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class HttpLogger(private val logger: Logger = DEFAULT) : Interceptor {

    companion object {

        private const val MAX_LOGGABLE_BODY = 1048576
        private val UTF8 = Charset.forName("UTF-8")

        /** A [Logger] defaults output appropriate for the current platform.  */
        private val DEFAULT: Logger = object : Logger {
            override fun log(message: String) {
                Platform.get().log(message, INFO, null)
            }
        }

    }

    enum class Level {
        NONE, BASIC, HEADERS, BODY
    }

    @Volatile
    private var headersToRedact = emptySet<String>()
    @Volatile
    var level = Level.NONE

    /** Change the level at which this interceptor logs.  */
    fun setLevel(level: Level): HttpLogger {
        this.level = level
        return this
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level

        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request.body

        val connection = chain.connection()
        var requestStartMessage = "--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}"
        if (!logHeaders && requestBody != null) {
            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
        }
        logger.log(requestStartMessage)

        if (logHeaders) {
            if (requestBody != null) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logger.log("Content-Type: ${requestBody.contentType()}")
                }
                if (requestBody.contentLength() != -1L) {
                    logger.log("Content-Length: ${requestBody.contentLength()}")
                }
            }

            val headers = request.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                    logHeader(headers, i)
                }
                i++
            }

            if (!logBody || requestBody == null) {
                logger.log("--> END ${request.method}")
            } else if (bodyHasUnknownEncoding(request.headers)) {
                logger.log("--> END ${request.method} (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                var charset = UTF8
                val contentType = requestBody.contentType()
                var isFile = false
                if (contentType != null) {
                    charset = contentType.charset(UTF8)

                    isFile = when (contentType.type) {
                        "image", "audio", "video" -> true
                        else -> false
                    }
                }

                logger.log("")
                if (requestBody is MultipartBody && requestBody.contentLength() > MAX_LOGGABLE_BODY) {
                    val body = buffer.readString(charset)
                    val boundary = "--${requestBody.boundary}"
                    val parts = mutableListOf<String>()
                    var index = body.indexOf(boundary, 0)
                    requestBody.parts.forEach {
                        val content = "Content-Length: ${it.body.contentLength()}"
                        val end = body.indexOf(content, index)
                        parts.add("${body.substring(index, end)}\n$content\n\n~PART BODY~")
                        index = body.indexOf(boundary, end)
                    }
                    parts.add("$boundary--")
                    parts.forEach { logger.log(it) }
                } else if (isPlaintext(buffer) && !isFile) {
                    logger.log(buffer.readString(charset))
                    logger.log("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
                } else {
                    logger.log("--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)")
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log("<-- HTTP FAILED: $e")
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body
        val contentLength = responseBody?.contentLength() ?: -1L
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        logger.log(
            "<-- ${response.code} ${if (response.message.isEmpty()) "" else " ${response.message}"}" +
                    " ${response.request.url} (${tookMs}ms${if (!logHeaders) ", $bodySize body" else ""})"
        )

        if (logHeaders) {
            val headers = response.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                logHeader(headers, i)
                i++
            }

            if (!logBody || !response.promisesBody() || responseBody == null) {
                logger.log("<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers)) {
                logger.log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer

                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    var gzippedResponseBody: GzipSource? = null
                    try {
                        gzippedResponseBody = GzipSource(buffer.clone())
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    } finally {
                        gzippedResponseBody?.close()
                    }
                }

                var charset = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                if (!isPlaintext(buffer)) {
                    logger.log("")
                    logger.log("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
                    return response
                }

                if (contentLength != 0L) {
                    logger.log("")
                    logger.log(buffer.clone().readString(charset))
                }

                if (gzippedLength != null) {
                    logger.log("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
                } else {
                    logger.log("<-- END HTTP (${buffer.size}-byte body)")
                }
            }
        }

        return response
    }

    private fun logHeader(headers: Headers, i: Int) {
        val value = if (headersToRedact.contains(headers.name(i))) "██" else headers.value(i)
        logger.log("${headers.name(i)}: $value")
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }

    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return (contentEncoding != null
                && !contentEncoding.equals("identity", ignoreCase = true)
                && !contentEncoding.equals("gzip", ignoreCase = true))
    }

    interface Logger {
        fun log(message: String)
    }
}