package com.pharmacy.myapp.auth.model

import androidx.navigation.NavDirections

sealed class AuthResult {
    data class PopBack(val popBackId: Int) : AuthResult()
    data class Direction(val direction: NavDirections) : AuthResult()
    data class DirectionId(val directionId: Int) : AuthResult()
}