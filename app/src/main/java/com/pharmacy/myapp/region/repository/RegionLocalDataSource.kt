package com.pharmacy.myapp.region.repository

import com.pharmacy.myapp.model.TemporaryRegion
import com.pharmacy.myapp.model.region.RegionDAO

class RegionLocalDataSource(private val dao: RegionDAO) {

    suspend fun setRegion(temporaryRegion: TemporaryRegion) = dao.insert(temporaryRegion)

}