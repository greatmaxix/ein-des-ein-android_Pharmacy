package com.pharmacy.myapp.search.repository

import com.pharmacy.myapp.user.repository.UserRepository

class SearchRepository(private val rds: SearchRemoteDataSource, private val userRepository: UserRepository) {

    suspend fun searchPaging(productName: String?, page: Int = 1, pageSize: Int = 10) = rds.globalSearch(productName, page, pageSize)

    suspend fun isCustomerExist() = userRepository.isCustomerExist()
}