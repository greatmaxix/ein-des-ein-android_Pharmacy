package com.pharmacy.myapp.product.repository

import com.pharmacy.myapp.model.product.RecentlyViewedDAO
import com.pharmacy.myapp.product.model.Product

class ProductLocalDataSource(private val dao: RecentlyViewedDAO) {

    suspend fun getRecentlyViewed() = dao.getList()

    suspend fun save(products: List<Product>) {
        dao.clear()
        dao.insert(products)
    }

}