package com.pharmacy.myapp.checkout

import com.pharmacy.myapp.checkout.dialog.PromoCodeDialogFragment
import com.pharmacy.myapp.checkout.repository.CheckoutLocalDataSource
import com.pharmacy.myapp.checkout.repository.CheckoutRemoteDataSource
import com.pharmacy.myapp.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val checkoutModule = module {

    single { CheckoutRepository(get(), get()) }
    single { CheckoutRemoteDataSource(get()) }
    single { CheckoutLocalDataSource(get<DBManager>().customerDAO) }

    viewModel { CheckoutViewModel(get()) }

    fragment { CheckoutFragment(get()) }

    fragment { PromoCodeDialogFragment() }
}