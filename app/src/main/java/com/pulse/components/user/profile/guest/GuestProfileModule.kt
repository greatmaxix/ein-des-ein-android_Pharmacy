package com.pulse.components.user.profile.guest

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val guestProfileModule = module {

    viewModel { GuestProfileViewModel(get()) }
}