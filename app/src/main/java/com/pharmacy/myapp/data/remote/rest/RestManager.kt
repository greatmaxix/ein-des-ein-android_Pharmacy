package com.pharmacy.myapp.data.remote.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.core.utils.HttpLogger
import com.pharmacy.myapp.data.remote.rest.interceptor.HeaderInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class RestManager {

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
        getInterceptors().forEach { addInterceptor(it) }
    }.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private fun getInterceptors(): ArrayList<Interceptor> {
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

    suspend fun signUp(name: String, phone: String, email: String) =
        api.signUp(mapOf("username" to name, "email" to email, "phone" to phone))

    suspend fun auth(phone: String) = api.auth(mapOf("phone" to phone))

    suspend fun login(phone: String, code: String) =
        api.login(mapOf("phone" to phone, "code" to code))

}