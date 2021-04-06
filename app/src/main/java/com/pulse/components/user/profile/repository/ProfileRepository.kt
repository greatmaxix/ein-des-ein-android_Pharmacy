package com.pulse.components.user.profile.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.components.user.model.addressAndNote.AddressDAO
import com.pulse.components.user.model.customer.Customer
import com.pulse.components.user.model.customer.CustomerDAO
import com.pulse.core.extensions.clearAll
import com.pulse.core.extensions.getOnes
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.Region.FIELD_REGION_ID
import com.pulse.data.local.Preferences.Token.FIELD_REFRESH_TOKEN
import com.pulse.data.remote.RestConstants
import com.pulse.data.remote.RestConstants.AVATAR_UUID
import com.pulse.data.remote.RestConstants.EMAIL
import com.pulse.data.remote.RestConstants.NAME
import com.pulse.data.remote.api.RestApi
import com.pulse.model.product.RecentlyViewedDAO
import okhttp3.MultipartBody

class ProfileRepository(
    private val dataStore: DataStore<Preferences>,
    private val ra: RestApi,
    private val customerDao: CustomerDAO,
    private val recentlyViewedDAO: RecentlyViewedDAO,
    private val addressDAO: AddressDAO
) {

    fun getCustomerInfo() = customerDao.customerFlow()

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) {
        val updateCustomerInfo = ra.updateCustomerInfo(mapOf(NAME to name, EMAIL to email, AVATAR_UUID to avatarUuid))
        updateCustomerInfo.data.item.region?.regionId?.let { dataStore.put(FIELD_REGION_ID, it) }
        saveCustomerInfo(updateCustomerInfo.data.item)
    }

    private suspend fun saveCustomerInfo(customer: Customer) = customerDao.update(customer)

    suspend fun logout() {
        val refreshToken = dataStore.getOnes(FIELD_REFRESH_TOKEN)
        if (refreshToken.isNullOrEmpty()) throw Exception("Refresh token is empty")
        ra.logout(mapOf(RestConstants.REFRESH_TOKEN to (refreshToken ?: "")))
    }

    suspend fun clearCustomerData(customer: Customer) {
        customerDao.delete(customer)
        recentlyViewedDAO.clear()
        addressDAO.clear()
        dataStore.clearAll()
    }

    suspend fun uploadImage(partBody: MultipartBody.Part) = ra.uploadImage(partBody)
}