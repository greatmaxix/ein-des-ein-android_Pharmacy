package com.pharmacy.myapp.region.repository

import com.pharmacy.myapp.model.region.TemporaryRegion
import com.pharmacy.myapp.model.region.RegionDAO
import kotlinx.coroutines.flow.Flow

class RegionLocalDataSource(private val dao: RegionDAO) {

    fun clear() = dao.clear()

    suspend fun setRegion(temporaryRegion: TemporaryRegion) = dao.insert(temporaryRegion)

    fun getTemporaryRegion(): Flow<TemporaryRegion?> = dao.get()
}