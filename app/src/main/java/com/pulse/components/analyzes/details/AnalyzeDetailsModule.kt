package com.pulse.components.analyzes.details

import com.pulse.components.analyzes.details.repository.AnalyzeDetailsLocalDataSource
import com.pulse.components.analyzes.details.repository.AnalyzeDetailsRemoteDataSource
import com.pulse.components.analyzes.details.repository.AnalyzeDetailsRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val analyzeDetailsModule = module {

    single { AnalyzeDetailsRepository(get(), get()) }
    single { AnalyzeDetailsRemoteDataSource(get()) }
    single { AnalyzeDetailsLocalDataSource() }

    viewModel { AnalyzeDetailsViewModel(get()) }
}