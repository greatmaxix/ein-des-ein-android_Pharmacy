package com.pharmacy.myapp.checkoutMap

import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val checkoutMapModule = module {
    fragment { CheckoutMapFragment() }
    fragment { CheckoutListFragment() }
    fragment { CheckoutMapContainerFragment() }
}