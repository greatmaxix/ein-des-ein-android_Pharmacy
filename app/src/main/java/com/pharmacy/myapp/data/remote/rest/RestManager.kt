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
import com.pharmacy.myapp.data.remote.rest.RestConstants.Companion.REGION_ID
import com.pharmacy.myapp.data.remote.rest.request.TokenRefreshRequest
import com.pharmacy.myapp.data.remote.rest.serializer.DeliveryTypeDeserializer
import com.pharmacy.myapp.data.remote.rest.serializer.DeliveryTypeSerializer
import com.pharmacy.myapp.data.remote.rest.serializer.OrderStatusDeserializer
import com.pharmacy.myapp.data.remote.rest.serializer.OrderStatusSerializer
import com.pharmacy.myapp.model.order.OrderStatus
import com.pharmacy.myapp.orders.model.CreateOrderRequest
import com.pharmacy.myapp.orders.model.DeliveryType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestManager(private val sp: SPManager) {

    companion object {
        private const val BASE_URL = "https://api.pharmacies.fmc-dev.com" /*"https://api.pharmacies.release.fmc-dev.com"*/ //TODO change to release in future
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
            sp.token = ""
            val refreshToken = sp.refreshToken
            if (refreshToken.isNullOrBlank()) throw IllegalArgumentException("refreshToken is empty")
            val response = api.tokenRefresh(TokenRefreshRequest(refreshToken))
            sp.token = response.token
            sp.refreshToken = response.refreshToken
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
            registerTypeAdapter(DeliveryType::class.java, DeliveryTypeDeserializer())
            registerTypeAdapter(DeliveryType::class.java, DeliveryTypeSerializer())
            registerTypeAdapter(OrderStatus::class.java, OrderStatusDeserializer())
            registerTypeAdapter(OrderStatus::class.java, OrderStatusSerializer())
            setLenient()
        }.create().also { gson = it })

    private fun createClient() = OkHttpClient.Builder().apply {
        connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)
        addInterceptor {
            val original = it.request()
            val token = sp.token
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

    suspend fun fetchCustomer() = safeApiCall(tokenRefreshCall) { api.fetchCustomer() }

    suspend fun productSearch(page: Int? = null, pageSize: Int? = null, barCode: String? = null, categoryCode: String? = null, name: String? = null) =
        safeApiCall(tokenRefreshCall) { api.productSearch(page, pageSize, sp.regionId, barCode, categoryCode, name) }

    suspend fun getProductById(globalProductId: Int) = safeApiCall(tokenRefreshCall) { api.getProductById(globalProductId) }

    suspend fun setToWishList(globalProductId: Int) = safeApiCall(tokenRefreshCall) { api.setToWishList(globalProductId) }

    suspend fun removeFromWishList(globalProductId: Int) = safeApiCall(tokenRefreshCall) { api.removeFromWishList(globalProductId) }

    suspend fun getWishList(page: Int? = null, pageSize: Int? = null) = safeApiCall(tokenRefreshCall) { api.getWishList(page, pageSize) }

    suspend fun regions() = safeApiCall(tokenRefreshCall) { api.regions() }

    suspend fun updateRegion(id: Int) = safeApiCall(tokenRefreshCall) { api.updateRegion(mapOf(REGION_ID to id)) }

    suspend fun getCategories() = safeApiCall(tokenRefreshCall) { api.categories() }

    suspend fun getPharmacyList(globalProductId: Int, page: Int? = null, pageSize: Int? = null) =
        safeApiCall(tokenRefreshCall) { api.pharmacyList(globalProductId, sp.regionId, page, pageSize) }

    suspend fun getCartProducts() = safeApiCall(tokenRefreshCall) { api.cartProducts() }

    suspend fun addProductToCart(globalProductId: Int) = safeApiCall(tokenRefreshCall) { api.addProductToCart(globalProductId) }

    suspend fun removeProductFromCart(globalProductId: Int) = safeApiCall(tokenRefreshCall) { api.removeProductFromCart(globalProductId) }

    suspend fun sendOrder(body: CreateOrderRequest) = safeApiCall(tokenRefreshCall) { api.sendOrder(body) }

    suspend fun fetchOrders(query: String, page: Int? = null, pageSize: Int? = null) = safeApiCall(tokenRefreshCall) { api.fetchOrders(query, page, pageSize) }

    suspend fun getOrderDetail(id: Int) = safeApiCall(tokenRefreshCall) { api.getOrderDetail(id) }

    suspend fun cancelOrder(id: Int) = safeApiCall(tokenRefreshCall) { api.cancelOrder(id) }
}