package com.pulse.components.user.repository

import com.pulse.data.remote.api.RestApi

class CustomerRemoteDataSource(private val api: RestApi) {

    suspend fun fetchCustomer() = api.fetchCustomer()

}