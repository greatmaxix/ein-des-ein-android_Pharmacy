package com.pharmacy.myapp.data.remote.rest

import com.pharmacy.myapp.data.remote.rest.response.LoginResponse
import com.pharmacy.myapp.data.remote.rest.response.UserDataResponse
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("$API_PATH/registration")
    suspend fun signUp(@Body arguments: Map<String, String>): Response<JSONObject>

    @POST("$API_PATH/auth")
    suspend fun auth(@Body arguments: Map<String, String>): JSONObject

    @POST("$API_PATH/login")
    suspend fun login(@Body arguments: Map<String, String>): LoginResponse

    @PATCH("$API_PATH/customer")
    suspend fun updateCustomerData(@Body arguments: Map<String, String>): UserDataResponse

    @POST("$API_PATH/logout")
    suspend fun logout(@Body arguments: Map<String, String>): Response<JSONObject>

    @Multipart
    @POST("$API_PATH/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<JSONObject>

    companion object {
        private const val API_PATH = "/api/v1/customer"
    }
}