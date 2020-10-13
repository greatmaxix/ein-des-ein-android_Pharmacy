package com.pharmacy.user.wishlist.repository

class WishRepository(private val rds: WishRemoteDataSource) {

    private suspend fun setToWishList(globalProductId: Int) = rds.setToWishList(globalProductId)

    private suspend fun removeFromWishList(globalProductId: Int) = rds.removeFromWishList(globalProductId)

    suspend fun setOrRemoveWish(setOrRemove: Pair<Boolean, Int>) = setOrRemove.run { if (first) setToWishList(second) else removeFromWishList(second) }

    suspend fun getWishList(page: Int? = null, pageSize: Int? = null) = rds.getWishList(page, pageSize)

}