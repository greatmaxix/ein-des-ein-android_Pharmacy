package com.pulse.model.product

import androidx.room.Dao
import androidx.room.Query
import com.pulse.core.db.BaseDao
import com.pulse.product.model.Product

@Dao
interface RecentlyViewedDAO: BaseDao<Product> {

    @Query("SELECT * FROM Product LIMIT 2")
    suspend fun getList(): List<Product>

    @Query("DELETE FROM Product")
    fun clear()
}