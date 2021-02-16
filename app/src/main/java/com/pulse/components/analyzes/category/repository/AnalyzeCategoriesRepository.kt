package com.pulse.components.analyzes.category.repository

class AnalyzeCategoriesRepository(private val rds: AnalyzeCategoriesRemoteDataSource, private val lds: AnalyzeCategoriesLocalDataSource) {

    suspend fun getAnalyzeCategories() = rds.getCategories()
}