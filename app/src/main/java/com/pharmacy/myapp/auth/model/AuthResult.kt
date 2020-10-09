package com.pharmacy.myapp.auth.model

import androidx.navigation.NavDirections

data class AuthResult(val popBackId: Int? = null, val direction: NavDirections? = null) {

    companion object {
        fun newInstancePopBack(popBackId: Int) = AuthResult(popBackId)

        fun newInstanceDirection(direction: NavDirections) = AuthResult(direction = direction)
    }
}