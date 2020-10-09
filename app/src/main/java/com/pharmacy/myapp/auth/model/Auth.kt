package com.pharmacy.myapp.auth.model

data class Auth(val name: String, val phone: String, val email: String, val code: String = "") {

    companion object {
        fun singUpInstance(name: String, phone: String, email: String) = Auth(name, phone, email)
    }
}