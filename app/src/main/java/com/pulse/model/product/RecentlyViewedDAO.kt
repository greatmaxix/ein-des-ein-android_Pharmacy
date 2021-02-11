package com.pulse.model.product

import androidx.room.Dao
import androidx.room.Query
import com.pulse.components.product.model.Product
import com.pulse.core.db.BaseDao

@Dao
interface RecentlyViewedDAO: BaseDao<Product> {

    @Query("SELECT * FROM Product LIMIT 2")
    suspend fun getList(): List<Product>

    @Query("DELETE FROM Product")
    fun clear()
}