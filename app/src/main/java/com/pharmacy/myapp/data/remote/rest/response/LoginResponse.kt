package com.pharmacy.myapp.data.remote.rest.response

data class LoginResponse(val customer: Customer, val refresh_token: String, val token: String)

data class Customer(val email: String, val phone: String, val username: String, val uuid: String)