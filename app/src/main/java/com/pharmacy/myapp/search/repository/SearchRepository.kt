package com.pharmacy.myapp.search.repository

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.product.repository.ProductRepository

class SearchRepository(private val rds: SearchRemoteDataSource, private val searchRepository: ProductRepository, spm: SPManager) {

    suspend fun searchPaging(productName: String?, page: Int = 1, pageSize: Int = 10) =
        rds.globalSearch(name = productName, page = page, pageSize = pageSize)




    suspend fun productById(globalProductId: Int) = searchRepository.productById(globalProductId)

}