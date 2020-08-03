package com.pharmacy.myapp.order

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager

class OrderRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)