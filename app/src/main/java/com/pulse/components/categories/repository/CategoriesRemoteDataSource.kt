package com.pulse.components.categories.repository

import com.pulse.data.remote.api.RestApi

class CategoriesRemoteDataSource(private val ra: RestApi) {

    suspend fun getCategories() = ra.categories()
}