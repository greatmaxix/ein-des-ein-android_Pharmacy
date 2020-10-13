package com.pharmacy.devTools

import com.pharmacy.data.local.SPManager
import com.pharmacy.data.remote.RestManager

class DevToolsRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)