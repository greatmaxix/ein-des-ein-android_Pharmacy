package com.pulse.components.analyzes.order

import com.pulse.components.analyzes.order.repository.AnalyzeOrderRepository
import com.pulse.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzeOrderViewModel(private val repository: AnalyzeOrderRepository) : BaseViewModel()