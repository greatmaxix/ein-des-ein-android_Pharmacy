package com.pharmacy.myapp.model.region

import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.myapp.core.db.BaseDao
import com.pharmacy.myapp.model.TemporaryRegion

@Dao
interface RegionDAO : BaseDao<TemporaryRegion> {

    @Query("select * from temporary_region limit 1")
    fun get(): TemporaryRegion?
}