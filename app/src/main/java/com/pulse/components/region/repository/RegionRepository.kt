package com.pulse.components.region.repository

import com.pulse.components.user.model.customer.Customer
import com.pulse.components.user.model.customer.CustomerItem
import com.pulse.components.user.repository.CustomerLocalDataSource
import com.pulse.core.network.ResponseWrapper
import com.pulse.model.BaseDataResponse
import com.pulse.model.region.LocalRegion
import com.pulse.model.region.Region
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class RegionRepository(private val rrds: RegionRemoteDataSource, private val rlds: RegionLocalDataSource, private val lds: CustomerLocalDataSource) : KoinComponent {

    suspend fun getRegions() = rrds.getRegions()

    suspend fun saveRegionLocally(region: Region) {
        rlds.clear()
        rlds.setRegion(LocalRegion(region.id, region.name))
        rlds.setLocalRegion(region.id)
    }

    suspend fun updateCustomerRegion(id: Int): ResponseWrapper<BaseDataResponse<CustomerItem>> {
        rlds.setLocalRegion(id)
        return rrds.updateCustomerRegion(id)
    }

    suspend fun getCustomer() = lds.getCustomer()

    suspend fun saveCustomerInfoLocally(customer: Customer) = lds.save(customer)

    fun getTemporaryRegion(): Flow<LocalRegion?> = rlds.getTemporaryRegion()
}