package com.pulse.devTools

import com.pulse.data.local.SPManager
import com.pulse.data.remote.RestManager

class DevToolsRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)