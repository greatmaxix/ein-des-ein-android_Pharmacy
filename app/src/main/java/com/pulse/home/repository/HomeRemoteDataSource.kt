package com.pulse.home.repository

import com.pulse.data.remote.api.RestApi

class HomeRemoteDataSource(private val restApi: RestApi) {

    suspend fun getCategories() = restApi.categories()

}