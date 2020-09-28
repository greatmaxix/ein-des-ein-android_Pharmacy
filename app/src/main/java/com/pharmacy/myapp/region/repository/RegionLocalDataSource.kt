package com.pharmacy.myapp.region.repository

import com.pharmacy.myapp.model.region.LocalRegion
import com.pharmacy.myapp.model.region.RegionDAO

class RegionLocalDataSource(private val dao: RegionDAO) {

    fun clear() = dao.clear()

    suspend fun setRegion(region: LocalRegion) = dao.insert(region)

    fun getTemporaryRegion() = dao.get()
}