package com.pulse.components.auth.model

import androidx.navigation.NavDirections

sealed class AuthResult {
    data class PopBack(val popBackId: Int) : AuthResult()
    data class Direction(val direction: NavDirections) : AuthResult()
}