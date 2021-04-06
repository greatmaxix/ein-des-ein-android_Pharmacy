package com.pulse.components.payments

import com.pulse.components.payments.repository.PaymentsRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val paymentsModule = module {

    single { PaymentsRepository() }

    viewModel { PaymentsViewModel(get()) }
}