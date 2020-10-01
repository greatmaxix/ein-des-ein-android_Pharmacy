package com.pharmacy.myapp.region.repository

import com.pharmacy.myapp.core.network.ResponseWrapper
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.RestManager
import com.pharmacy.myapp.model.BaseDataResponse
import com.pharmacy.myapp.user.model.customer.CustomerItem

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