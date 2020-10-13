package com.pharmacy.product.repository

import com.pharmacy.data.remote.RestManager

class ProductRemoteDataSource(private val rm: RestManager) {

    suspend fun getProductById(globalProductId: Int) = rm.getProductById(globalProductId)


}