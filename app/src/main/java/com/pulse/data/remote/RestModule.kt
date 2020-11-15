package com.pulse.data.remote

import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.pulse.BuildConfig
import com.pulse.data.remote.api.RestApi
import com.pulse.data.remote.api.RestApiRefresh
import com.pulse.data.remote.authenticator.RestAuthenticator
import com.pulse.data.remote.deserializer.CategoryDeserializer
import com.pulse.data.remote.interceptor.RestHeaderInterceptor
import com.pulse.data.remote.model.order.DeliveryType
import com.pulse.data.remote.serializer.*
import com.pulse.model.category.Category
import com.pulse.model.order.OrderStatus
import com.pulse.util.Constants
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
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

        val developBaseURL = "https://api.pharmacies.fmc-dev.com"
        val releaseBaseURL = "https://api.pharmacies.release.fmc-dev.com"

        Retrofit.Builder()
            .baseUrl(if (Constants.DEV_ENVIRONMENT) developBaseURL else releaseBaseURL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }

    fun makeLoggingInterceptor() = LoggingInterceptor.Builder()
        .setLevel(Level.BASIC)
        .log(Platform.INFO)
        .tag("OkHttp")
        .build()

    single {
        OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(120, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)

            authenticator(RestAuthenticator())
            addInterceptor(RestHeaderInterceptor(get()))

            if (BuildConfig.DEBUG) {
                interceptors().add(makeLoggingInterceptor())
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
            registerTypeAdapter(Category::class.java, CategoryDeserializer())
            setLenient()
        }.create()
    }
}