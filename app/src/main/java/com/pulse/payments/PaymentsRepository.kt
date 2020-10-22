package com.pulse.payments

import com.pulse.data.local.SPManager
import com.pulse.data.remote.RestManager

class PaymentsRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)