package com.pulse.components.categories.model

import androidx.room.Dao
import androidx.room.Query
import com.pulse.core.db.BaseDao
import com.pulse.model.category.Category

@Dao
interface CategoryDAO : BaseDao<Category> {

    @Query("select * from category")
    suspend fun categories(): List<Category>

    @Query("SELECT COUNT(*) FROM category")
    suspend fun categoriesSize(): Int

    @Query("select * from category limit 1")
    fun get(): Category?

    @Query("DELETE FROM category")
    fun clear()
}