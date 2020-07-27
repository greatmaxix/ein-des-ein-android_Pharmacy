package com.pharmacy.myapp.splash

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import com.pharmacy.myapp.splash.SplashViewModel.Companion.UPDATE_CUSTOMER_INFO
import com.pharmacy.myapp.splash.worker.UpdateCustomerInfoWorker
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val splashModule = module {
    viewModel { SplashViewModel(get(), get()) }
    fragment { SplashFragment(get()) }

    single { SplashRepository(get(), get()) }

    factory(named(UPDATE_CUSTOMER_INFO)) {
        OneTimeWorkRequestBuilder<UpdateCustomerInfoWorker>().setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        ).build()
    }
}