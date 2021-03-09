package com.pulse.components.analyzes.checkout.repository

import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class AnalyzeCheckoutRepository(private val rds: AnalyzeCheckoutRemoteDataSource, private val lds: AnalyzeCheckoutLocalDataSource) : KoinComponent {

    fun getCustomerInfo() = lds.getCustomerInfo()
}