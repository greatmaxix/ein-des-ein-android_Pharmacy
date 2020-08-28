package com.pharmacy.myapp.user.wishlist.repository

import com.pharmacy.myapp.data.remote.rest.RestManager

class WishRemoteDataSource(private val rm: RestManager) {

    suspend fun setToWishList(globalProductId: Int) = rm.setToWishList(globalProductId)

    suspend fun removeFromWishList(globalProductId: Int) = rm.removeFromWishList(globalProductId)

    suspend fun getWishList(page: Int? = null, pageSize: Int? = null) = rm.getWishList(page, pageSize)
}