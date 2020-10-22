package com.pulse.splash.repository

import com.pulse.data.remote.RestManager

class SplashRemoteDataSource(private val rm: RestManager) {

    suspend fun fetchCustomer() = rm.fetchCustomer()

}