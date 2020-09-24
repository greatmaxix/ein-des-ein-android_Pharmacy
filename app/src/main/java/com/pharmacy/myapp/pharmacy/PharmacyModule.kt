package com.pharmacy.myapp.pharmacy

import com.pharmacy.myapp.pharmacy.tabs.list.PharmacyListFragment
import com.pharmacy.myapp.pharmacy.tabs.map.PharmacyMapBottomSheet
import com.pharmacy.myapp.pharmacy.tabs.map.PharmacyMapFragment
import com.pharmacy.myapp.pharmacy.repository.PharmacyRemoteDataSource
import com.pharmacy.myapp.pharmacy.repository.PharmacyRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val checkoutMapModule = module {
    fragment { PharmacyFragment() }
    fragment { PharmacyMapFragment() }
    fragment { PharmacyListFragment() }
    fragment { PharmacyMapBottomSheet() }

    single { PharmacyRemoteDataSource(get()) }
    single { PharmacyRepository(get(), get(), get()) }

    viewModel { (globalProductId: Int) -> PharmacyViewModel(globalProductId, get()) }
}