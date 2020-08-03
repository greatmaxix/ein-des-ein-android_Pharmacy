package com.pharmacy.myapp.checkout

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager

class CheckoutRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)