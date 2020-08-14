package com.pharmacy.myapp.data.remote.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.network.ResponseWrapper
import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.core.utils.HttpLogger
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestConstants.Companion.AVATAR_UUID
import com.pharmacy.myapp.data.remote.rest.RestConstants.Companion.CODE
import com.pharmacy.myapp.data.remote.rest.RestConstants.Companion.EMAIL
import com.pharmacy.myapp.data.remote.rest.RestConstants.Companion.NAME
import com.pharmacy.myapp.data.remote.rest.RestConstants.Companion.PHONE
import com.pharmacy.myapp.data.remote.rest.RestConstants.Companion.REFRESH_TOKEN
import com.pharmacy.myapp.data.remote.rest.request.TokenRefreshRequest
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestManager : KoinComponent {

    private val spManager: SPManager by inject()

    companion object {
        private const val BASE_URL = "https://api.pharmacies.fmc-dev.com" // "https://api.pharmacies.release.fmc-dev.com" TODO change to release in future
        private const val READ_TIMEOUT = 30L
        private const val CONNECT_TIMEOUT = 60L
        private const val WRITE_TIMEOUT = 120L
    }

    private lateinit var api: ApiService
    private lateinit var gson: Gson

    val tokenRefreshCall: suspend () -> ResponseWrapper<Any> = {
        safeApiCall({
            ResponseWrapper.Error(
                R.string.error_networkErrorMessage,
                "Token refresh error due to recursive refresh call"
            ) // TODO check this case
        }) {
            spManager.token = ""
            val refreshToken = spManager.refreshToken
            if (refreshToken.isNullOrBlank()) throw IllegalArgumentException("refreshToken is empty")
            val response = api.tokenRefresh(TokenRefreshRequest(refreshToken))
            spManager.token = response.token
            spManager.refreshToken = response.refreshToken
            Any()
        }
    }

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
                it.proceed(original.newBuilder().header("Authorization", "Bearer $token").build())
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
        api.signUp(mapOf(NAME to name, EMAIL to email, PHONE to phone))

    suspend fun auth(phone: String) = api.auth(mapOf(PHONE to phone))

    suspend fun login(phone: String, code: String) =
        api.login(mapOf(PHONE to phone, CODE to code))

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) =
        api.updateCustomerInfo(mapOf(NAME to name, EMAIL to email, AVATAR_UUID to avatarUuid))

    suspend fun logout(refreshToken: String) = api.logout(mapOf(REFRESH_TOKEN to refreshToken))

    suspend fun uploadImage(partBody: MultipartBody.Part) = api.uploadImage(partBody)

    suspend fun fetchCustomerInfo() = api.fetchCustomerInfo()

    suspend fun productSearch(
        page: Int? = null,
        perPage: Int? = null,
        regionId: Int? = null,
        barCode: Int? = null,
        name: String? = null
    ) = api.productSearch(page, perPage, regionId, barCode, name)

    suspend fun regions() = api.regions()
}