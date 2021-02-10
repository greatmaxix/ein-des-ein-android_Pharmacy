package com.pulse.components.user.address

import com.pulse.components.user.address.repository.AddressLocalDataSource
import com.pulse.components.user.address.repository.AddressRepository
import com.pulse.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addressModule = module {

    single { AddressLocalDataSource(get<DBManager>().addressDAO) }
    single { AddressRepository(get()) }

    viewModel { AddressViewModel(get()) }

    fragment { AddressFragment(get()) }
}