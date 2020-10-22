package com.pulse.region.repository

import com.pulse.model.region.LocalRegion
import com.pulse.model.region.RegionDAO

class RegionLocalDataSource(private val dao: RegionDAO) {

    fun clear() = dao.clear()

    suspend fun setRegion(region: LocalRegion) = dao.insert(region)

    fun getTemporaryRegion() = dao.get()
}