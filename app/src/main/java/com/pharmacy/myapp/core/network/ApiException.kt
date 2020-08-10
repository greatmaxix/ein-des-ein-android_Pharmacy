package com.pharmacy.myapp.core.network

import com.pharmacy.myapp.model.Violation

data class ApiException(
    override val message: String,
    val type: String,
    val violations: List<Violation>
) : Exception(message)