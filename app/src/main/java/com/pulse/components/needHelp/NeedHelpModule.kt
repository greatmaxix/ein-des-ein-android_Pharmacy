package com.pulse.components.needHelp

import com.pulse.components.needHelp.repository.NeedHelpLocalDataSource
import com.pulse.components.needHelp.repository.NeedHelpRemoteDataSource
import com.pulse.components.needHelp.repository.NeedHelpRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val needHelpModule = module {

    single { NeedHelpRepository(get(), get()) }
    single { NeedHelpLocalDataSource() }
    single { NeedHelpRemoteDataSource(get()) }

    viewModel { NeedHelpViewModel(get()) }
}