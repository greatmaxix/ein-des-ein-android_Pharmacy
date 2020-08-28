package com.pharmacy.myapp.region

import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.model.TemporaryRegion
import com.pharmacy.myapp.model.region.Region
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.user.model.customerInfo.CustomerInfo
import org.koin.core.KoinComponent
import org.koin.core.get

class RegionRepository(private val restManager: RestManager, private val customerDAO: CustomerDAO) : KoinComponent {

    private val regionDAO by lazy { get<DBManager>().regionDAO }

    suspend fun getRegions() = restManager.regions()

    suspend fun saveRegionLocally(region: Region) = regionDAO.insert(TemporaryRegion(region.id, region.name))

    suspend fun updateCustomerRegion(id: Int) = restManager.updateRegion(id)

    suspend fun getCustomer() = customerDAO.getCustomer()

    suspend fun saveCustomerInfoLocally(customer: CustomerInfo) = customerDAO.save(customer)
}