package com.pharmacy.myapp.checkoutMap

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val checkoutMapModule = module {
    fragment { CheckoutMapContainerFragment() }
    fragment { CheckoutMapFragment() }
    fragment { CheckoutListFragment() }
    fragment { DrugstoreBottomSheet() }

    viewModel { CheckoutMapViewModel() }
}