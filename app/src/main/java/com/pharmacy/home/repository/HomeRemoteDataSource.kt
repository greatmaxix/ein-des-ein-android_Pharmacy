package com.pharmacy.home.repository

import com.pharmacy.data.remote.api.RestApi

class HomeRemoteDataSource(private val restApi: RestApi) {

    suspend fun getCategories() = restApi.categories()

}