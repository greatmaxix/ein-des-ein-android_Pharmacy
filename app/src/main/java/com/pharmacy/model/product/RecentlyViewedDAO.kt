package com.pharmacy.model.product

import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.core.db.BaseDao
import com.pharmacy.product.model.Product

@Dao
interface RecentlyViewedDAO: BaseDao<Product> {

    @Query("SELECT * FROM Product LIMIT 2")
    suspend fun getList(): List<Product>

    @Query("DELETE FROM Product")
    fun clear()
}