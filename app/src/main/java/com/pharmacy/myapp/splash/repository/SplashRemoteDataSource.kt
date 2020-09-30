package com.pharmacy.myapp.splash.repository

import com.pharmacy.myapp.data.remote.rest.RestManager

class SplashRemoteDataSource(private val rm: RestManager) {

    suspend fun fetchCustomer() = rm.fetchCustomer()

}