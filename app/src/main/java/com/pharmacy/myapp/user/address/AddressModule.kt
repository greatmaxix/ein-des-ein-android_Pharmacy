package com.pharmacy.myapp.user.address

import com.pharmacy.myapp.user.address.repository.AddressRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addressModule = module {

    single { AddressRepository() }

    viewModel { AddressViewModel(get()) }

    fragment { AddressFragment(get()) }
}