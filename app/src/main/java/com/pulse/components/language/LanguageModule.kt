package com.pulse.components.language

import com.pulse.components.language.repository.LanguageLocalDataSource
import com.pulse.components.language.repository.LanguageRemoteDataSource
import com.pulse.components.language.repository.LanguageRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val languageModule = module {

    single { LanguageRepository(get(), get()) }
    single { LanguageRemoteDataSource(get()) }
    single { LanguageLocalDataSource(get()) }

    viewModel { LanguageViewModel(get()) }
}