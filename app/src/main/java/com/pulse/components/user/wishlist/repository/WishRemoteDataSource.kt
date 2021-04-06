package com.pulse.components.user.wishlist.repository

import com.pulse.data.remote.api.RestApi

class WishRemoteDataSource(private val ra: RestApi) {

    suspend fun setToWishList(globalProductId: Int) = ra.setToWishList(globalProductId)

    suspend fun removeFromWishList(globalProductId: Int) = ra.removeFromWishList(globalProductId)

    suspend fun getWishList(page: Int? = null, pageSize: Int? = null) = ra.getWishList(page, pageSize)
}