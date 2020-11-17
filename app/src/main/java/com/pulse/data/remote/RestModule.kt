package com.pulse.data.remote

import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.pulse.BuildConfig.*
import com.pulse.components.recipes.model.RecipeStatus
import com.pulse.data.remote.api.RestApi
import com.pulse.data.remote.api.RestApiRefresh
import com.pulse.data.remote.authenticator.RestAuthenticator
import com.pulse.data.remote.deserializer.CategoryDeserializer
import com.pulse.data.remote.deserializer.RecipesStatusDeserializer
import com.pulse.data.remote.interceptor.RestHeaderInterceptor
import com.pulse.data.remote.model.order.DeliveryType
import com.pulse.data.remote.serializer.*
import com.pulse.model.category.Category
import com.pulse.model.order.OrderStatus
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
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
        Retrofit.Builder()
            .baseUrl(if (DEVELOPER_SERVER) "https://api.pharmacies.fmc-dev.com" else "https://api.pharmacies.release.fmc-dev.com")
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }

    single {

        val ihsanbalLoggingInterceptor = LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .tag("OkHttp")
            .build()

        val okhttpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(120, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)

            authenticator(RestAuthenticator())
            addInterceptor(RestHeaderInterceptor(get()))
            addInterceptor(if (DEBUG && IHSANBAL) ihsanbalLoggingInterceptor else okhttpLoggingInterceptor)

        }.build()
    }

    single {
        GsonBuilder().apply {
            registerTypeAdapter(String::class.java, StringDeserializer())
            registerTypeAdapter(DeliveryType::class.java, DeliveryTypeDeserializer())
            registerTypeAdapter(DeliveryType::class.java, DeliveryTypeSerializer())
            registerTypeAdapter(OrderStatus::class.java, OrderStatusDeserializer())
            registerTypeAdapter(RecipeStatus::class.java, RecipesStatusDeserializer())
            registerTypeAdapter(OrderStatus::class.java, OrderStatusSerializer())
            registerTypeAdapter(LocalDateTime::class.java, DateTimeSerializer())
            registerTypeAdapter(Category::class.java, CategoryDeserializer())
            setLenient()
        }.create()
    }
}