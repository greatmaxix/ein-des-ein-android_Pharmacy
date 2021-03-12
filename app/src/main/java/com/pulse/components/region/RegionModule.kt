package com.pulse.components.region

import com.pulse.components.region.repository.RegionLocalDataSource
import com.pulse.components.region.repository.RegionRemoteDataSource
import com.pulse.components.region.repository.RegionRepository
import com.pulse.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val regionModule = module {

    single { RegionRepository(get(), get(), get()) }
    single { RegionRemoteDataSource(get()) }
    single { RegionLocalDataSource(get<DBManager>().regionDAO, get()) }

    viewModel { RegionViewModel(get()) }

    fragment { RegionFragment(get()) }
}