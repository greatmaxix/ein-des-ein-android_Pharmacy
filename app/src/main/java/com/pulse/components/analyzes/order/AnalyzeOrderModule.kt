package com.pulse.components.analyzes.order

import com.pulse.components.analyzes.order.repository.AnalyzeOrderLocalDataSource
import com.pulse.components.analyzes.order.repository.AnalyzeOrderRemoteDataSource
import com.pulse.components.analyzes.order.repository.AnalyzeOrderRepository
import com.pulse.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val analyzeOrderModule = module {

    single { AnalyzeOrderRepository(get(), get()) }
    single { AnalyzeOrderRemoteDataSource(get()) }
    single { AnalyzeOrderLocalDataSource(get<DBManager>().customerDAO) }

    viewModel { AnalyzeOrderViewModel(get()) }
}