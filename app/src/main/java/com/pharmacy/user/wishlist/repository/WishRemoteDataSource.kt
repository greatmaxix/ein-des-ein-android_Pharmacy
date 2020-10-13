package com.pharmacy.user.wishlist.repository

import com.pharmacy.data.remote.RestManager

class WishRemoteDataSource(private val rm: RestManager) {

    suspend fun setToWishList(globalProductId: Int) = rm.setToWishList(globalProductId)

    suspend fun removeFromWishList(globalProductId: Int) = rm.removeFromWishList(globalProductId)

    suspend fun getWishList(page: Int? = null, pageSize: Int? = null) = rm.getWishList(page, pageSize)
}