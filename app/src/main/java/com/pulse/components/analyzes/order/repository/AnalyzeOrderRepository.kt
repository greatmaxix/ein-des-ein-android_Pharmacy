package com.pulse.components.analyzes.order.repository

import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class AnalyzeOrderRepository(private val rds: AnalyzeOrderRemoteDataSource, private val lds: AnalyzeOrderLocalDataSource) : KoinComponent {

    fun getCustomerInfo() = lds.getCustomerInfo()
}