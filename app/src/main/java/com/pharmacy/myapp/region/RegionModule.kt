package com.pharmacy.myapp.region

import com.pharmacy.myapp.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val regionModule = module {

    single { RegionRepository(get(), get<DBManager>().customerDAO()) }

    viewModel { RegionViewModel(get()) }

    fragment { RegionFragment(get()) }
}