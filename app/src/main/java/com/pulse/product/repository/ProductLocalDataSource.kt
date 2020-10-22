package com.pulse.product.repository

import androidx.room.Transaction
import com.pulse.model.product.RecentlyViewedDAO
import com.pulse.product.model.Product

class ProductLocalDataSource(private val dao: RecentlyViewedDAO) {

    suspend fun getRecentlyViewed() = dao.getList()

    @Transaction
    suspend fun save(products: List<Product>) {
        dao.clear()
        dao.insert(products)
    }

}