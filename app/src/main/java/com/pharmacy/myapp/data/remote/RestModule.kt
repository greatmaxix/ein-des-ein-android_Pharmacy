package com.pharmacy.myapp.data.remote

import com.google.gson.GsonBuilder
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.data.remote.api.RestApi
import com.pharmacy.myapp.data.remote.api.RestApiRefresh
import com.pharmacy.myapp.data.remote.authenticator.RestAuthenticator
import com.pharmacy.myapp.data.remote.interceptor.RestHeaderInterceptor
import com.pharmacy.myapp.data.remote.model.order.DeliveryType
import com.pharmacy.myapp.data.remote.serializer.*
import com.pharmacy.myapp.model.order.OrderStatus
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@OptIn(KoinApiExtension::class)
val RESTModule = module {

    single { get<Retrofit>().create(RestApi::class.java) }
    single { get<Retrofit>().create(RestApiRefresh::class.java) }

    single {

        val baseURL = "https://api.pharmacies.fmc-dev.com" /*"https://api.pharmacies.release.fmc-dev.com"*/ //TODO change to release in future

        Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }

    single {
        OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(120, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)

            authenticator(RestAuthenticator())
            addInterceptor(RestHeaderInterceptor(get()))

            if (BuildConfig.DEBUG) {
                interceptors().add(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }

    single {
        GsonBuilder().apply {
            registerTypeAdapter(String::class.java, StringDeserializer())
            registerTypeAdapter(DeliveryType::class.java, DeliveryTypeDeserializer())
            registerTypeAdapter(DeliveryType::class.java, DeliveryTypeSerializer())
            registerTypeAdapter(OrderStatus::class.java, OrderStatusDeserializer())
            registerTypeAdapter(OrderStatus::class.java, OrderStatusSerializer())
            registerTypeAdapter(LocalDateTime::class.java, DateTimeSerializer())
            setLenient()
        }.create()
    }
}