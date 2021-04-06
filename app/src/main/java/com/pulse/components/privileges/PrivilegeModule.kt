package com.pulse.components.privileges

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val privilegeModule = module {

    viewModel { PrivilegeViewModel() }
}