package com.pulse.components.analyzes.category.repository

import com.pulse.data.remote.DummyData
import com.pulse.data.remote.api.RestApi

class AnalyzeCategoriesRemoteDataSource(private val rm: RestApi) {

    suspend fun getCategories(id: Int?) = DummyData.analyzeCategories // TODO rm.getAnalyzeCategories()
}