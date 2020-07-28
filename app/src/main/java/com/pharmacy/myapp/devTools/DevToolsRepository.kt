package com.pharmacy.myapp.devTools

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager

class DevToolsRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)