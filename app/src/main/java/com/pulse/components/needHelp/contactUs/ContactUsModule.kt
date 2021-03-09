package com.pulse.components.needHelp.contactUs

import com.pulse.components.needHelp.contactUs.repository.ContactUsRemoteDataSource
import com.pulse.components.needHelp.contactUs.repository.ContactUsRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val contactUsModule = module {

    single { ContactUsRepository(get()) }
    single { ContactUsRemoteDataSource(get()) }

    viewModel { ContactUsViewModel(get()) }

    fragment { ContactUsFragment(get()) }
}