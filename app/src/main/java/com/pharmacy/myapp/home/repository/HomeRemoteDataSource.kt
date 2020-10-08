package com.pharmacy.myapp.home.repository

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.remote.api.RestApi

class HomeRemoteDataSource(private val restApi: RestApi) {

    suspend fun getCategories() = safeApiCall { restApi.categories() }

}