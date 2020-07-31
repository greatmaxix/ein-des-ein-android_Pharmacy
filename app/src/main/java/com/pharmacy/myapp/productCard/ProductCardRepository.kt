package com.pharmacy.myapp.productCard

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager

class ProductCardRepository(
    private val spManager: SPManager,
    private val rm: RestManager
)