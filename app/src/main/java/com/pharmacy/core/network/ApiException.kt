package com.pharmacy.core.network

import com.pharmacy.model.Violation

data class ApiException(
    override val message: String,
    val type: String,
    val violations: List<Violation>
) : Exception(message)