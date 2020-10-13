package com.pharmacy.splash.repository

import com.pharmacy.data.remote.RestManager

class SplashRemoteDataSource(private val rm: RestManager) {

    suspend fun fetchCustomer() = rm.fetchCustomer()

}