package com.pharmacy.myapp.model.product

import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.myapp.core.db.BaseDao
import com.pharmacy.myapp.product.model.Product

@Dao
interface RecentlyViewedDAO: BaseDao<Product> {

    @Query("SELECT * FROM Product LIMIT 2")
    suspend fun getList(): List<Product>

    @Query("DELETE FROM Product")
    fun clear()
}