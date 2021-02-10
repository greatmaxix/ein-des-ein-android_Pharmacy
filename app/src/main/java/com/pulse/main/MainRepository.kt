package com.pulse.main

import com.pulse.components.user.model.customer.CustomerDAO
import com.pulse.data.local.SPManager
import com.pulse.data.remote.api.RestApi

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