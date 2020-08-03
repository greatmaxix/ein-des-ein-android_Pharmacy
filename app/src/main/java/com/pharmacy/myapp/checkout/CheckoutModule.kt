package com.pharmacy.myapp.checkout

import com.pharmacy.myapp.checkout.dialog.PromoCodeDialogFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val checkoutModule = module {

    single { CheckoutRepository(get(), get()) }

    viewModel { CheckoutViewModel(get()) }

    fragment { CheckoutFragment(get()) }

    fragment { PromoCodeDialogFragment() }
}