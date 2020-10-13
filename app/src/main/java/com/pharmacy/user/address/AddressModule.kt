package com.pharmacy.user.address

import com.pharmacy.data.local.DBManager
import com.pharmacy.user.address.repository.AddressLocalDataSource
import com.pharmacy.user.address.repository.AddressRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addressModule = module {

    single { AddressLocalDataSource(get<DBManager>().addressDAO) }
    single { AddressRepository(get()) }

    viewModel { AddressViewModel(get()) }

    fragment { AddressFragment(get()) }
}