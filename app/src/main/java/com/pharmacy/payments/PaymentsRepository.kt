package com.pharmacy.payments

import com.pharmacy.data.local.SPManager
import com.pharmacy.data.remote.RestManager

class PaymentsRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)