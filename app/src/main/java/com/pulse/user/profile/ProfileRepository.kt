package com.pulse.user.profile

import com.pulse.core.network.safeApiCall
import com.pulse.data.local.SPManager
import com.pulse.data.remote.RestManager
import com.pulse.model.product.RecentlyViewedDAO
import com.pulse.user.model.addressAndNote.AddressDAO
import com.pulse.user.model.customer.Customer
import com.pulse.user.model.customer.CustomerDAO
import okhttp3.MultipartBody

class ProfileRepository(
    private val spManager: SPManager,
    private val rm: RestManager,
    private val customerDao: CustomerDAO,
    private val recentlyViewedDAO: RecentlyViewedDAO,
    private val addressDAO: AddressDAO
) {

    fun getCustomerInfo() = customerDao.customerLiveData()

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) =
        safeApiCall {
            val updateCustomerInfo = rm.updateCustomerInfo(name, email, avatarUuid)
            spManager.regionId = updateCustomerInfo.data.item.region?.regionId
            saveCustomerInfo(updateCustomerInfo.data.item)
        }

    private suspend fun saveCustomerInfo(customer: Customer) = customerDao.update(customer)

    suspend fun logout() =
        safeApiCall {
            if (spManager.refreshToken.isNullOrEmpty()) throw Exception("Refresh token is empty")
            rm.logout(spManager.refreshToken ?: "")
        }

    suspend fun clearCustomerData(customer: Customer) {
        customerDao.delete(customer)
        recentlyViewedDAO.clear()
        addressDAO.clear()
        spManager.clear()
    }

    suspend fun uploadImage(partBody: MultipartBody.Part) =
        safeApiCall { rm.uploadImage(partBody) }
}