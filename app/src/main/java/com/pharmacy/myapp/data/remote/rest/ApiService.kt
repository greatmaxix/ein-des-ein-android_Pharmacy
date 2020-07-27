package com.pharmacy.myapp.data.remote.rest

import com.pharmacy.myapp.data.remote.rest.request.TokenRefreshRequest
import com.pharmacy.myapp.data.remote.rest.response.LoginResponse
import com.pharmacy.myapp.data.remote.rest.response.TokenRefreshResponse
import com.pharmacy.myapp.data.remote.rest.response.UploadImageResponse
import com.pharmacy.myapp.model.BaseDataResponse
import com.pharmacy.myapp.model.CustomerResponse
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("$API_PATH/token/refresh")
    suspend fun tokenRefresh(@Body body: TokenRefreshRequest): TokenRefreshResponse

    @POST("$API_PATH_CUSTOMER/registration")
    suspend fun signUp(@Body arguments: Map<String, String>): Response<JSONObject>

    @POST("$API_PATH_CUSTOMER/auth")
    suspend fun auth(@Body arguments: Map<String, String>): JSONObject

    @POST("$API_PATH_CUSTOMER/login")
    suspend fun login(@Body arguments: Map<String, String>): LoginResponse

    @PATCH("$API_PATH_CUSTOMER/customer")
    suspend fun updateCustomerInfo(@Body arguments: Map<String, String>): BaseDataResponse<CustomerResponse>

    @POST("$API_PATH_CUSTOMER/logout")
    suspend fun logout(@Body arguments: Map<String, String>): Response<JSONObject>

    @Multipart
    @POST("$API_PATH_CUSTOMER/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): UploadImageResponse

    @GET("$API_PATH_CUSTOMER/customer")
    suspend fun fetchCustomerInfo(): BaseDataResponse<CustomerResponse>

    companion object {
        private const val API_PATH = "/api/v1"
        private const val API_PATH_CUSTOMER = "$API_PATH/customer"
    }
}