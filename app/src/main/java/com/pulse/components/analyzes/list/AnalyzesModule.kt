package com.pulse.components.analyzes.list

import com.pulse.components.analyzes.list.repository.AnalyzesLocalDataSource
import com.pulse.components.analyzes.list.repository.AnalyzesRemoteDataSource
import com.pulse.components.analyzes.list.repository.AnalyzesRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val analyzesModule = module {

    single { AnalyzesRepository(get(), get()) }
    single { AnalyzesRemoteDataSource(get()) }
    single { AnalyzesLocalDataSource() }

    viewModel { AnalyzesViewModel(get()) }
}