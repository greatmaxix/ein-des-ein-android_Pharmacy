package com.pulse.components.checkout

import com.pulse.components.checkout.dialog.PromoCodeDialogFragment
import com.pulse.components.checkout.repository.CheckoutLocalDataSource
import com.pulse.components.checkout.repository.CheckoutRemoteDataSource
import com.pulse.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val checkoutModule = module {

    single { CheckoutRepository(get(), get()) }
    single { CheckoutRemoteDataSource(get()) }
    single { CheckoutLocalDataSource(get<DBManager>().customerDAO, get<DBManager>().addressDAO) }

    viewModel { CheckoutViewModel(get()) }

    fragment { CheckoutFragment(get()) }

    fragment { PromoCodeDialogFragment() }
}