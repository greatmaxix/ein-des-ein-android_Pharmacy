package com.pharmacy.myapp.splash.repository

import com.pharmacy.myapp.data.remote.RestManager

class SplashRemoteDataSource(private val rm: RestManager) {

    suspend fun fetchCustomer() = rm.fetchCustomer()

}