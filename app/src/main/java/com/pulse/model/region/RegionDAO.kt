package com.pulse.model.region

import androidx.room.Dao
import androidx.room.Query
import com.pulse.core.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface RegionDAO : BaseDao<LocalRegion> {

    @Query("select * from local_region limit 1")
    fun get(): Flow<LocalRegion?>

    @Query("DELETE FROM local_region")
    fun clear()
}