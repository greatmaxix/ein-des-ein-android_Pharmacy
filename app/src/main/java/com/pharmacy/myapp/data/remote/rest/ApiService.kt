package com.pharmacy.myapp.data.remote.rest

import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("$API_PATH$CUSTOMER_PATH/registration")
    suspend fun registration(
        @Query("username") name: String,
        @Query("email") email: String,
        @Query("phone") phone: String
    ): String/*BaseResponse<RegistrationResponse>*/


    @POST("$API_PATH$CUSTOMER_PATH/auth")
    suspend fun phone(@Query("phone") phone: String): String/*BaseResponse<AuthResponse>*/

    companion object {
        private const val API_PATH = "/api/v1"
        private const val CUSTOMER_PATH = "/customer"
    }
}