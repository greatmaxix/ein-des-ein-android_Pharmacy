package com.pulse.components.product.repository

import com.pulse.data.remote.RestManager

class ProductRemoteDataSource(private val rm: RestManager) {

    suspend fun getProductById(globalProductId: Int) = rm.getProductById(globalProductId)


}