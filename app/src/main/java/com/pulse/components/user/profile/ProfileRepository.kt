package com.pulse.components.user.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.components.user.model.addressAndNote.AddressDAO
import com.pulse.components.user.model.customer.Customer
import com.pulse.components.user.model.customer.CustomerDAO
import com.pulse.core.extensions.clearAll
import com.pulse.core.extensions.getOnes
import com.pulse.core.extensions.put
import com.pulse.core.network.safeApiCall
import com.pulse.data.local.Preferences.Region.FIELD_REGION_ID
import com.pulse.data.local.Preferences.Token.FIELD_REFRESH_TOKEN
import com.pulse.data.remote.RestManager
import com.pulse.model.product.RecentlyViewedDAO
import okhttp3.MultipartBody

class ProfileRepository(
    private val dataStore: DataStore<Preferences>,
    private val rm: RestManager,
    private val customerDao: CustomerDAO,
    private val recentlyViewedDAO: RecentlyViewedDAO,
    private val addressDAO: AddressDAO
) {

    fun getCustomerInfo() = customerDao.customerLiveData()

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) =
        safeApiCall {
            val updateCustomerInfo = rm.updateCustomerInfo(name, email, avatarUuid)
            updateCustomerInfo.data.item.region?.regionId?.let { dataStore.put(FIELD_REGION_ID, it) }
            saveCustomerInfo(updateCustomerInfo.data.item)
        }

    private suspend fun saveCustomerInfo(customer: Customer) = customerDao.update(customer)

    suspend fun logout() =
        safeApiCall {
            val refreshToken = dataStore.getOnes(FIELD_REFRESH_TOKEN)
            if (refreshToken.isNullOrEmpty()) throw Exception("Refresh token is empty")
            rm.logout(refreshToken ?: "")
        }

    suspend fun clearCustomerData(customer: Customer) {
        customerDao.delete(customer)
        recentlyViewedDAO.clear()
        addressDAO.clear()
        dataStore.clearAll()
    }

    suspend fun uploadImage(partBody: MultipartBody.Part) =
        safeApiCall { rm.uploadImage(partBody) }
}