package com.pharmacy.myapp.region

import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.region.repository.RegionLocalDataSource
import com.pharmacy.myapp.region.repository.RegionRemoteDataSource
import com.pharmacy.myapp.region.repository.RegionRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val regionModule = module {

    single { RegionRepository(get(), get(), get()) }
    single { RegionRemoteDataSource(get()) }
    single { RegionLocalDataSource(get<DBManager>().regionDAO) }

    viewModel { RegionViewModel(get()) }

    fragment { RegionFragment(get()) }
}