package com.pharmacy.myapp.checkout.repository

import com.pharmacy.myapp.data.remote.rest.RestManager

class CheckoutRemoteDataSource(private val rm: RestManager) {

    suspend fun sendOrder(mock: String) = rm.sendOrder(mock)

}