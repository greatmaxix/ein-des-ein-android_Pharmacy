package com.pharmacy.myapp.data.remote

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.RestConstants.AVATAR_UUID
import com.pharmacy.myapp.data.remote.RestConstants.CODE
import com.pharmacy.myapp.data.remote.RestConstants.EMAIL
import com.pharmacy.myapp.data.remote.RestConstants.NAME
import com.pharmacy.myapp.data.remote.RestConstants.PHONE
import com.pharmacy.myapp.data.remote.RestConstants.REFRESH_TOKEN
import com.pharmacy.myapp.data.remote.RestConstants.REGION_ID
import com.pharmacy.myapp.data.remote.api.RestApi
import com.pharmacy.myapp.data.remote.model.order.CreateOrderRequest
import okhttp3.MultipartBody

@Deprecated("")
class RestManager(private val sp: SPManager, private val RestApi: RestApi) {
    suspend fun signUp(name: String, phone: String, email: String) =
        RestApi.signUp(mapOf(NAME to name, EMAIL to email, PHONE to phone))

    suspend fun auth(phone: String) = RestApi.signIn(mapOf(PHONE to phone))

    suspend fun login(phone: String, code: String) =
        RestApi.signCode(mapOf(PHONE to phone, CODE to code))

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) =
        RestApi.updateCustomerInfo(mapOf(NAME to name, EMAIL to email, AVATAR_UUID to avatarUuid))

    suspend fun logout(refreshToken: String) = RestApi.logout(mapOf(REFRESH_TOKEN to refreshToken))

    suspend fun uploadImage(partBody: MultipartBody.Part) = RestApi.uploadImage(partBody)

    suspend fun fetchCustomer() = safeApiCall { RestApi.fetchCustomer() }

    suspend fun productSearch(page: Int? = null, pageSize: Int? = null, barCode: String? = null, categoryCode: String? = null, name: String? = null) =
        safeApiCall { RestApi.productSearch(page, pageSize, sp.regionId, barCode, categoryCode, name) }

    suspend fun getProductById(globalProductId: Int) = safeApiCall { RestApi.getProductById(globalProductId) }

    suspend fun setToWishList(globalProductId: Int) = safeApiCall { RestApi.setToWishList(globalProductId) }

    suspend fun removeFromWishList(globalProductId: Int) = safeApiCall { RestApi.removeFromWishList(globalProductId) }

    suspend fun getWishList(page: Int? = null, pageSize: Int? = null) = safeApiCall { RestApi.getWishList(page, pageSize) }

    suspend fun regions() = safeApiCall { RestApi.regions() }

    suspend fun updateRegion(id: Int) = safeApiCall { RestApi.updateRegion(mapOf(REGION_ID to id)) }

    suspend fun getCategories() = safeApiCall { RestApi.categories() }

    suspend fun getPharmacyList(globalProductId: Int, page: Int? = null, pageSize: Int? = null) =
        safeApiCall { RestApi.pharmacyList(globalProductId, sp.regionId, page, pageSize) }

    suspend fun getCartProducts() = safeApiCall { RestApi.cartProducts() }

    suspend fun addProductToCart(globalProductId: Int) = safeApiCall { RestApi.addProductToCart(globalProductId) }

    suspend fun removeProductFromCart(globalProductId: Int) = safeApiCall { RestApi.removeProductFromCart(globalProductId) }

    suspend fun sendOrder(body: CreateOrderRequest) = safeApiCall { RestApi.sendOrder(body) }

    suspend fun fetchOrders(query: String, page: Int? = null, pageSize: Int? = null) = safeApiCall { RestApi.fetchOrders(query, page, pageSize) }

    suspend fun getOrderDetail(id: Int) = safeApiCall { RestApi.getOrderDetail(id) }

    suspend fun cancelOrder(id: Int) = safeApiCall { RestApi.cancelOrder(id) }

}