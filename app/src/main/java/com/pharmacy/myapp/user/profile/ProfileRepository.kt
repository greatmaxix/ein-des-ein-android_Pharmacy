package com.pharmacy.myapp.user.profile

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.model.product.RecentlyViewedDAO
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.user.model.customerInfo.Customer
import okhttp3.MultipartBody

class ProfileRepository(private val spManager: SPManager, private val rm: RestManager, private val customerDao: CustomerDAO, private val recentlyViewedDAO: RecentlyViewedDAO) {

    fun getCustomerInfo() = customerDao.customerLiveData()

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) =
        safeApiCall(rm.tokenRefreshCall) {
            val updateCustomerInfo = rm.updateCustomerInfo(name, email, avatarUuid)
            spManager.regionId = updateCustomerInfo.data.item.region?.regionId
            saveCustomerInfo(updateCustomerInfo.data.item)
        }

    private suspend fun saveCustomerInfo(customer: Customer) = customerDao.update(customer)

    suspend fun logout() =
        safeApiCall(rm.tokenRefreshCall) {
            if (spManager.refreshToken.isNullOrEmpty()) throw Exception("Refresh token is empty")
            rm.logout(spManager.refreshToken ?: "")
        }

    suspend fun clearCustomerData(customer: Customer) {
        customerDao.delete(customer)
        recentlyViewedDAO.clear()
        spManager.clear()
    }

    suspend fun uploadImage(partBody: MultipartBody.Part) =
        safeApiCall(rm.tokenRefreshCall) { rm.uploadImage(partBody) }
}