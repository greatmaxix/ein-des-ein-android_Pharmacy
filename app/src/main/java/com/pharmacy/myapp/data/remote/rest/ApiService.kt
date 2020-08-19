package com.pharmacy.myapp.data.remote.rest

import com.pharmacy.myapp.data.remote.rest.request.TokenRefreshRequest
import com.pharmacy.myapp.data.remote.rest.response.AuthResponse
import com.pharmacy.myapp.data.remote.rest.response.TokenRefreshResponse
import com.pharmacy.myapp.data.remote.rest.response.UploadImageResponse
import com.pharmacy.myapp.model.*
import com.pharmacy.myapp.model.customerInfo.CustomerInfoItem
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("$API_PATH_CUSTOMER/refresh_token")
    suspend fun tokenRefresh(@Body body: TokenRefreshRequest): TokenRefreshResponse

    @POST("$API_PATH_CUSTOMER/registration")
    suspend fun signUp(@Body arguments: Map<String, String>): BaseDataResponse<AuthResponse>

    @POST("$API_PATH_CUSTOMER/auth")
    suspend fun auth(@Body arguments: Map<String, String>): JSONObject

    @POST("$API_PATH_CUSTOMER/login")
    suspend fun login(@Body arguments: Map<String, String>): BaseDataResponse<AuthResponse>

    @PUT("$API_PATH_CUSTOMER/customer")
    suspend fun updateCustomerInfo(@Body arguments: Map<String, String>): BaseDataResponse<CustomerInfoItem>

    @POST("$API_PATH_CUSTOMER/logout")
    suspend fun logout(@Body arguments: Map<String, String>): Response<JSONObject>

    @Multipart
    @POST("$API_PATH_CUSTOMER/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): BaseDataResponse<UploadImageResponse>

    @GET("$API_PATH_CUSTOMER/customer")
    suspend fun fetchCustomerInfo(): BaseDataResponse<CustomerInfoItem>

    @GET("$API_PATH_PUBLIC/products/search")
    suspend fun productSearch(
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("regionId") regionId: Int? = null,
        @Query("barCode") barCode: Int? = null,
        @Query("name") name: String? = null
    ): BaseDataResponse<PaginationModel<Product>>

    @GET("$API_PATH_PUBLIC/regions")
    suspend fun regions(): BaseDataResponse<ListItemsModel<Region>>

    // TODO specify proper moder for response
//    @GET("$API_PATH_PUBLIC/categories")
//    suspend fun categories(): BaseDataResponse<ListItemsModel<T>>

    companion object {

        private const val API_PATH = "/api/v1"
        private const val API_PATH_CUSTOMER = "$API_PATH/customer"
        private const val API_PATH_PUBLIC = "$API_PATH/public"
    }
}