package com.pulse.components.analyzes.checkout

import com.pulse.components.analyzes.checkout.repository.AnalyzeCheckoutLocalDataSource
import com.pulse.components.analyzes.checkout.repository.AnalyzeCheckoutRemoteDataSource
import com.pulse.components.analyzes.checkout.repository.AnalyzeCheckoutRepository
import com.pulse.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val analyzeCheckoutModule = module {

    single { AnalyzeCheckoutRepository(get(), get()) }
    single { AnalyzeCheckoutRemoteDataSource(get()) }
    single { AnalyzeCheckoutLocalDataSource(get<DBManager>().customerDAO) }

    viewModel { AnalyzeCheckoutViewModel(get()) }
}