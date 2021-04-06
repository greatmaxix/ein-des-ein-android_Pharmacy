package com.pulse.components.stub

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val stubModule = module {

    viewModel { StubViewModel() }
}