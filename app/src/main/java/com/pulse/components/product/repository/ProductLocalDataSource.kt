package com.pulse.components.product.repository

import androidx.room.Transaction
import com.pulse.components.product.model.Product
import com.pulse.model.product.RecentlyViewedDAO

class ProductLocalDataSource(private val dao: RecentlyViewedDAO) {

    suspend fun getRecentlyViewed() = dao.getList()

    @Transaction
    suspend fun save(products: List<Product>) {
        dao.clear()
        dao.insert(products)
    }

}