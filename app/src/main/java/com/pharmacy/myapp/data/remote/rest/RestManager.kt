package com.pharmacy.myapp.data.remote.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.core.utils.HttpLogger
import com.pharmacy.myapp.data.remote.rest.interceptor.HeaderInterceptor
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class RestManager {

    companion object {
        private const val TIME_OUT = 30L

        private val IS_DEBUG = BuildConfig.DEBUG
        private const val SCHEME = "https"
        private const val BASE_URL = " api.pharmacies.fmc-dev.com"
        private val HTTP_PORT: Int? = null
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
        baseUrl(createHttpUrl())
        addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        addConverterFactory(createGsonConverter())
        client(createClient())
    }.build()

    private fun createHttpUrl(): HttpUrl {
        val builder = HttpUrl.Builder().scheme(SCHEME).host(BASE_URL)/*.addPathSegment("/api/v1/customer")*/
        HTTP_PORT?.let { builder.port(it) }
        return builder.build()
    }

    private fun createGsonConverter() =
        GsonConverterFactory.create(GsonBuilder().apply {
            setLenient()
        }.create().also { gson = it })

    private fun createClient()= OkHttpClient.Builder().apply {
        getInterceptors().forEach { addInterceptor(it) }
    }.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    fun getInterceptors(): ArrayList<Interceptor> {
        val interceptors: ArrayList<Interceptor> = ArrayList()
        with(interceptors) {
            add(HeaderInterceptor())
            if (IS_DEBUG) {
                add(HttpLogger().apply {
                    level = HttpLogger.Level.BODY
                })
            }
        }

        return interceptors
    }


}