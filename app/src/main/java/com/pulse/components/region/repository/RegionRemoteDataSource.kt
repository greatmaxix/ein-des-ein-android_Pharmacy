package com.pulse.components.region.repository

import com.pulse.components.user.model.customer.CustomerItem
import com.pulse.core.network.ResponseWrapper
import com.pulse.data.local.SPManager
import com.pulse.data.remote.RestManager
import com.pulse.model.BaseDataResponse

class RegionRemoteDataSource(private val rm: RestManager, private val sp: SPManager) {

    suspend fun getRegions() = rm.regions()

    suspend fun updateCustomerRegion(id: Int): ResponseWrapper<BaseDataResponse<CustomerItem>> {
        setLocalRegion(id)
        return rm.updateRegion(id)
    }

    fun setLocalRegion(region: Int) {
        sp.regionId = region
    }
}