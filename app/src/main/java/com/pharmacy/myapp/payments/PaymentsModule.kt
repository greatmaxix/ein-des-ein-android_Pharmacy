package com.pharmacy.myapp.payments

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val paymentsModule = module {

    single { PaymentsRepository(get(), get()) }

    viewModel { PaymentsViewModel(get()) }

    fragment { PaymentsFragment() }
    fragment { AddPaymentMethodFragment() }
    fragment { AddCardFragment() }
}