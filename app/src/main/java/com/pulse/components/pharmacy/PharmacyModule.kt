package com.pulse.components.pharmacy

import com.pulse.components.pharmacy.repository.PharmacyRemoteDataSource
import com.pulse.components.pharmacy.repository.PharmacyRepository
import com.pulse.components.pharmacy.repository.PharmacyUseCase
import com.pulse.components.pharmacy.tabs.list.PharmacyListFragment
import com.pulse.components.pharmacy.tabs.map.PharmacyMapFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val pharmacyModule = module {

    fragment { PharmacyMapFragment() }
    fragment { PharmacyListFragment() }

    single { PharmacyRemoteDataSource(get()) }
    single { PharmacyRepository(get()) }
    single { PharmacyUseCase(get(), get()) }

    viewModel { (globalProductId: Int) -> PharmacyViewModel(globalProductId, get()) }
}