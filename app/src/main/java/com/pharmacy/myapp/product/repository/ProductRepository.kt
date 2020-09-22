package com.pharmacy.myapp.product.repository

import com.pharmacy.myapp.product.model.Product

class ProductRepository(private val rds: ProductRemoteDataSource, private val lds: ProductLocalDataSource) {

    suspend fun productById(globalProductId: Int) = rds.getProductById(globalProductId)

    suspend fun getRecentlyViewed() = lds.getRecentlyViewed()

    suspend fun saveRecentlyViewed(product: Product) {
        val lastProduct = getRecentlyViewed().firstOrNull()?.apply { primaryKey = 1 }
        if (lastProduct != product) {
            lds.save(listOfNotNull(product, lastProduct))
        }
    }
}