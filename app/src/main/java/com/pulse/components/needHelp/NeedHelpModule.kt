package com.pulse.components.needHelp

import com.pulse.components.needHelp.repository.NeedHelpLocalDataSource
import com.pulse.components.needHelp.repository.NeedHelpRemoteDataSource
import com.pulse.components.needHelp.repository.NeedHelpRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val needHelpModule = module {

    single { NeedHelpRepository(get(), get()) }
    single { NeedHelpLocalDataSource(get()) }
    single { NeedHelpRemoteDataSource(get()) }

    viewModel { NeedHelpViewModel(get()) }

    fragment { NeedHelpFragment(get()) }
}