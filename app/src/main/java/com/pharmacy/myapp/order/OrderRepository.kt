package com.pharmacy.myapp.order

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.RestManager

class OrderRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)