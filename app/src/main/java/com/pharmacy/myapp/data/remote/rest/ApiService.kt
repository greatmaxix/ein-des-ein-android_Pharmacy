package com.pharmacy.myapp.data.remote.rest

import com.pharmacy.myapp.data.remote.rest.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("$API_PATH/registration")
    suspend fun signUp(@Body arguments: Map<String, String>): Response<String>

    @POST("$API_PATH/auth")
    suspend fun auth(@Body arguments: Map<String, String>): Response<String>

    @POST("$API_PATH/login")
    suspend fun login(@Body arguments: Map<String, String>): LoginResponse

    companion object {
        private const val API_PATH = "/api/v1/customer"
    }
}