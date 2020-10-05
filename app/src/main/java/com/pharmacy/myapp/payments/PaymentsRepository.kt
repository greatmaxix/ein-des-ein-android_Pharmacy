package com.pharmacy.myapp.payments

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.RestManager

class PaymentsRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)