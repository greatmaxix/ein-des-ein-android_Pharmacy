package com.pulse.components.user.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.getOnes
import com.pulse.data.local.Preferences.Token.FIELD_ACCESS_TOKEN
import com.pulse.data.local.Preferences.Token.FIELD_REFRESH_TOKEN

class CustomerUseCase(private val repositoryCustomer: CustomerRepository, private val dataStore: DataStore<Preferences>) {

    private val isTokenExist
        get() = dataStore.getOnes(FIELD_ACCESS_TOKEN).isNullOrEmpty().not() && dataStore.getOnes(FIELD_REFRESH_TOKEN).isNullOrEmpty().not()

    suspend fun setCustomerRetrieveAvatarUrl() = if (isTokenExist) repositoryCustomer.setCustomerRetrieveAvatarUrl() else null
}