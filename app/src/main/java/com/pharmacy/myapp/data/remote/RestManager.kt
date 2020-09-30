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
import com.pharmacy.myapp.data.remote.api.RESTApi
import com.pharmacy.myapp.data.remote.api.RESTApiRefresh
import com.pharmacy.myapp.data.remote.model.order.CreateOrderRequest
import okhttp3.MultipartBody

@Deprecated("")
class RestManager(private val sp: SPManager, private val RESTApi: RESTApi) {
    suspend fun signUp(name: String, phone: String, email: String) =
        RESTApi.signUp(mapOf(NAME to name, EMAIL to email, PHONE to phone))

    suspend fun auth(phone: String) = RESTApi.auth(mapOf(PHONE to phone))

    suspend fun login(phone: String, code: String) =
        RESTApi.login(mapOf(PHONE to phone, CODE to code))

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) =
        RESTApi.updateCustomerInfo(mapOf(NAME to name, EMAIL to email, AVATAR_UUID to avatarUuid))

    suspend fun logout(refreshToken: String) = RESTApi.logout(mapOf(REFRESH_TOKEN to refreshToken))

    suspend fun uploadImage(partBody: MultipartBody.Part) = RESTApi.uploadImage(partBody)

    suspend fun fetchCustomer() = safeApiCall { RESTApi.fetchCustomer() }

    suspend fun productSearch(page: Int? = null, pageSize: Int? = null, barCode: String? = null, categoryCode: String? = null, name: String? = null) =
        safeApiCall { RESTApi.productSearch(page, pageSize, sp.regionId, barCode, categoryCode, name) }

    suspend fun getProductById(globalProductId: Int) = safeApiCall { RESTApi.getProductById(globalProductId) }

    suspend fun setToWishList(globalProductId: Int) = safeApiCall { RESTApi.setToWishList(globalProductId) }

    suspend fun removeFromWishList(globalProductId: Int) = safeApiCall { RESTApi.removeFromWishList(globalProductId) }

    suspend fun getWishList(page: Int? = null, pageSize: Int? = null) = safeApiCall { RESTApi.getWishList(page, pageSize) }

    suspend fun regions() = safeApiCall { RESTApi.regions() }

    suspend fun updateRegion(id: Int) = safeApiCall { RESTApi.updateRegion(mapOf(REGION_ID to id)) }

    suspend fun getCategories() = safeApiCall { RESTApi.categories() }

    suspend fun getPharmacyList(globalProductId: Int, page: Int? = null, pageSize: Int? = null) =
        safeApiCall { RESTApi.pharmacyList(globalProductId, sp.regionId, page, pageSize) }

    suspend fun getCartProducts() = safeApiCall { RESTApi.cartProducts() }

    suspend fun addProductToCart(globalProductId: Int) = safeApiCall { RESTApi.addProductToCart(globalProductId) }

    suspend fun removeProductFromCart(globalProductId: Int) = safeApiCall { RESTApi.removeProductFromCart(globalProductId) }

    suspend fun sendOrder(body: CreateOrderRequest) = safeApiCall { RESTApi.sendOrder(body) }

}