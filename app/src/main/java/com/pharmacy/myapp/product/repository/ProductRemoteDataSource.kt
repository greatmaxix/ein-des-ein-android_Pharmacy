package com.pharmacy.myapp.product.repository

import com.pharmacy.myapp.data.remote.rest.RestManager

class ProductRemoteDataSource(private val rm: RestManager) {

    suspend fun getProductById(globalProductId: Int) = rm.getProductById(globalProductId)


}