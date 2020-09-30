package com.pharmacy.myapp.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.data.remote.api.RESTApi
import com.pharmacy.myapp.data.remote.api.RESTApiRefresh
import com.pharmacy.myapp.data.remote.authenticator.RESTAuthenticator
import com.pharmacy.myapp.data.remote.interceptor.RESTHeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val RESTModule = module {

    single { get<Retrofit>().create(RESTApi::class.java) }
    single { get<Retrofit>().create(RESTApiRefresh::class.java) }

    single {

        val baseURL = "https://api.pharmacies.fmc-dev.com" /*"https://api.pharmacies.release.fmc-dev.com"*/ //TODO change to release in future

        Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .client(get())
            .build()
    }

    single {
        OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(120, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)

            authenticator(RESTAuthenticator())
            addInterceptor(RESTHeaderInterceptor(get()))

            if (BuildConfig.DEBUG) {
                interceptors().add(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }

    single {
        GsonBuilder().apply { setLenient() }.create()
    }
}