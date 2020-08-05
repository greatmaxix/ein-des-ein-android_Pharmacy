package com.pharmacy.myapp.cart

import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val cartModule = module {
    fragment { CartFragment() }
}