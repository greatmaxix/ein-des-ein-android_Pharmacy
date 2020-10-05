package com.pharmacy.myapp.region.repository

import com.pharmacy.myapp.model.region.LocalRegion
import com.pharmacy.myapp.model.region.Region
import com.pharmacy.myapp.user.model.customer.Customer
import com.pharmacy.myapp.user.repository.UserLocalDataSource
import kotlinx.coroutines.flow.Flow
import org.koin.core.KoinComponent

class RegionRepository(private val rrds: RegionRemoteDataSource, private val rlds: RegionLocalDataSource, private val lds: UserLocalDataSource) : KoinComponent {

    suspend fun getRegions() = rrds.getRegions()

    suspend fun saveRegionLocally(region: Region) {
        rlds.clear()
        rlds.setRegion(LocalRegion(region.id, region.name))
        rrds.setLocalRegion(region.id)
    }

    suspend fun updateCustomerRegion(id: Int) = rrds.updateCustomerRegion(id)

    suspend fun getCustomer() = lds.getCustomer()

    suspend fun saveCustomerInfoLocally(customer: Customer) = lds.save(customer)

    fun getTemporaryRegion(): Flow<LocalRegion?> = rlds.getTemporaryRegion()
}