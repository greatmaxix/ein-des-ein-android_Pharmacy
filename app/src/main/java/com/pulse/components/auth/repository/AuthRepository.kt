package com.pulse.components.auth.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.components.auth.model.Auth
import com.pulse.components.user.model.customer.Customer
import com.pulse.components.user.model.customer.CustomerDAO
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.Token.FIELD_ACCESS_TOKEN
import com.pulse.data.local.Preferences.Token.FIELD_REFRESH_TOKEN

class AuthRepository(private val dataStore: DataStore<Preferences>, private val dao: CustomerDAO, private val rds: AuthRemoteDataSource) {

    suspend fun signUp(auth: Auth) = rds.signUp(auth).dataOrThrow()

    suspend fun signIn(phone: String) = rds.signIn(phone).dataOrThrow()

    suspend fun signCode(phone: String, code: String) = with(rds.signCode(phone, code).dataOrThrow()) {
        saveToken(token, refreshToken)
        saveCustomer(customer)
    }

    private suspend fun saveToken(token: String, refreshToken: String) {
        dataStore.put(FIELD_ACCESS_TOKEN, token)
        dataStore.put(FIELD_REFRESH_TOKEN, refreshToken)
    }

    private suspend fun saveCustomer(customer: Customer): String? {
        dao.insert(customer)
        return customer.avatarInfo?.url
    }
}