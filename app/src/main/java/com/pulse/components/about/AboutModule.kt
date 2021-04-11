package com.pulse.components.about

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val aboutAppModule = module {

    viewModel {  AboutViewModel() }
}