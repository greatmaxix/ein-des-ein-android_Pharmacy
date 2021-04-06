package com.pulse.components.orders.details.repository

import com.pulse.data.remote.api.RestApi


class OrderDetailRemoteDataSource(private val ra: RestApi) {

    suspend fun cancelOrder(id: Int) = ra.cancelOrder(id)

}