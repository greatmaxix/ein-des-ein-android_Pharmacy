package com.pulse.components.analyzes.checkout.repository

import com.pulse.components.user.model.customer.CustomerDAO

class AnalyzeCheckoutLocalDataSource(private val dao: CustomerDAO) {

    fun getCustomerInfo() = dao.customerLiveData()
}