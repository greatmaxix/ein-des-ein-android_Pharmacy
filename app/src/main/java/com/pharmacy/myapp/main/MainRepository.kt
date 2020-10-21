package com.pharmacy.myapp.main

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.api.RestApi
import com.pharmacy.myapp.user.model.customer.CustomerDAO

class MainRepository(private val sp: SPManager, private val restApi: RestApi, private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

    fun customerLiveData() = dao.customerLiveData()

    fun setChatForeground(isForeground: Boolean) {
        sp.isChatForeground = isForeground
    }

    suspend fun getChat(chatId: Int) = restApi.getChat(chatId)
        .dataOrThrow()
        .item
}