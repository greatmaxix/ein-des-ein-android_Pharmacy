package com.pulse.components.region.repository

import com.pulse.data.remote.RestConstants
import com.pulse.data.remote.api.RestApi

class RegionRemoteDataSource(private val ra: RestApi) {

    suspend fun getRegions() = ra.regions()

    suspend fun updateCustomerRegion(id: Int) = ra.updateRegion(mapOf(RestConstants.REGION_ID to id))
}