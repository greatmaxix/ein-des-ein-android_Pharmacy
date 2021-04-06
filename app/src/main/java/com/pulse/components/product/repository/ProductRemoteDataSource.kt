package com.pulse.components.product.repository

import com.pulse.data.remote.api.RestApi

class ProductRemoteDataSource(private val ra: RestApi) {

    suspend fun getProductById(globalProductId: Int) = ra.getProductById(globalProductId)
}