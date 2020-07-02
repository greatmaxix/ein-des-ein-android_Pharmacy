package com.pharmacy.myapp.data.remote.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.core.utils.HttpLogger
import com.pharmacy.myapp.data.local.SPManager
import okhttp3.OkHttpClient
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestManager: KoinComponent {

    private val spManager: SPManager by inject()

    companion object {
        private val IS_DEBUG = BuildConfig.DEBUG
        private const val BASE_URL = "https://api.pharmacies.fmc-dev.com"
        private const val READ_TIMEOUT = 30L
        private const val CONNECT_TIMEOUT = 60L
        private const val WRITE_TIMEOUT = 120L
    }

    private lateinit var api: ApiService
    private lateinit var gson: Gson

    init {
        initServices(createRetrofit())
    }

    private fun initServices(retrofit: Retrofit) {
        api = retrofit.create(ApiService::class.java)
    }

    private fun createRetrofit() = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        addConverterFactory(createGsonConverter())
        client(createClient())
    }.build()

    private fun createGsonConverter() =
        GsonConverterFactory.create(GsonBuilder().apply {
            setLenient()
        }.create().also { gson = it })

    private fun createClient() = OkHttpClient.Builder().apply {
        connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)
        addInterceptor {
            val original = it.request()
            val token = spManager.token
            if (!token.isNullOrEmpty()) {
                it.proceed(original.newBuilder().header("Authorization", "Bearer: $token").build())
            } else {
                it.proceed(original)
            }
        }
        if (BuildConfig.DEBUG) {
            interceptors().add(HttpLogger().apply {
                level = HttpLogger.Level.BODY
            })
        }
    }.build()

    suspend fun signUp(name: String, phone: String, email: String) =
        api.signUp(mapOf("username" to name, "email" to email, "phone" to phone))

    suspend fun auth(phone: String) = api.auth(mapOf("phone" to phone))

    suspend fun login(phone: String, code: String) =
        api.login(mapOf("phone" to phone, "code" to code))

}