package com.pharmacy.myapp.data.remote.rest.response

data class UserDataResponse(
    val id: String,
    val username: String,
    val email: String,
    val uuid: String,
    val createdAt: String,
    val phone: String
)