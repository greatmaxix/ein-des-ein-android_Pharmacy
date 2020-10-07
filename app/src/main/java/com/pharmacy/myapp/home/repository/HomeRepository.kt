package com.pharmacy.myapp.home.repository

class HomeRepository(private val rds: HomeRemoteDataSource) {

    suspend fun getCategories() = rds.getCategories()

}