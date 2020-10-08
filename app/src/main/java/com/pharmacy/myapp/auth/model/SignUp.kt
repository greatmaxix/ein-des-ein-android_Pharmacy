package com.pharmacy.myapp.auth.model

data class SignUp(val name: String, val phone: String, val email: String) {

    companion object {
        fun newInstance(name: String, phone: String, email: String) = SignUp(name, phone, email)
    }
}