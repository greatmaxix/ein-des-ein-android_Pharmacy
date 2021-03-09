package com.pulse.components.analyzes.order.repository

import com.pulse.components.user.model.customer.CustomerDAO

class AnalyzeOrderLocalDataSource(private val dao: CustomerDAO) {

    fun getCustomerInfo() = dao.customerLiveData()
}