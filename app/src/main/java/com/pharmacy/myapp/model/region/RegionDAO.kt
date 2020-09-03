package com.pharmacy.myapp.model.region

import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.myapp.core.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface RegionDAO : BaseDao<TemporaryRegion> {

    @Query("select * from temporary_region limit 1")
    fun get(): Flow<TemporaryRegion?>

    @Query("DELETE FROM temporary_region")
    fun clear()
}