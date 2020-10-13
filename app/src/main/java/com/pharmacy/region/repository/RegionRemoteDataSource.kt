package com.pharmacy.region.repository

import com.pharmacy.core.network.ResponseWrapper
import com.pharmacy.data.local.SPManager
import com.pharmacy.data.remote.RestManager
import com.pharmacy.model.BaseDataResponse
import com.pharmacy.user.model.customer.CustomerItem

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