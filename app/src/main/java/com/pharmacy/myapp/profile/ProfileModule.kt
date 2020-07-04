package com.pharmacy.myapp.profile

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {

    single { ProfileRepository(get()) }

    viewModel { ProfileViewModel(get()) }

    fragment { ProfileFragment(get()) }

}