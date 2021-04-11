package com.pulse.components.analyzes.clinic.tabs

import com.pulse.components.analyzes.clinic.repository.ClinicTabsLocalDataSource
import com.pulse.components.analyzes.clinic.repository.ClinicTabsRemoteDataSource
import com.pulse.components.analyzes.clinic.repository.ClinicTabsRepository
import com.pulse.components.analyzes.clinic.tabs.list.ClinicListFragment
import com.pulse.components.analyzes.clinic.tabs.map.ClinicMapFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val clinicTabsModule = module {

    fragment { ClinicMapFragment() }
    fragment { ClinicListFragment() }

    single { ClinicTabsRemoteDataSource(get()) }
    single { ClinicTabsLocalDataSource() }
    single { ClinicTabsRepository(get(), get()) }

    viewModel { ClinicTabsViewModel(get()) }
}