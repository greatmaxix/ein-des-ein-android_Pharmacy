package com.pharmacy.myapp.product.repository

class ProductRepository(private val rds: ProductRemoteDataSource) {

    suspend fun productById(globalProductId: Int) = rds.getProductById(globalProductId)

}