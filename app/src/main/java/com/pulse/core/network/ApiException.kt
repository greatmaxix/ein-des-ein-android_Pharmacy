package com.pulse.core.network

import com.pulse.model.Violation

data class ApiException(
    override val message: String,
    val type: String,
    val violations: List<Violation>
) : Exception(message)