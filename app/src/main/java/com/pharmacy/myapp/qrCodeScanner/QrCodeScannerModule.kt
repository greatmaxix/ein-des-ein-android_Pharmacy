package com.pharmacy.myapp.qrCodeScanner

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val qrCodeScannerModule = module {

    single { QrCodeScannerRepository(get(), get()) }

    viewModel { QrCodeScannerViewModel(get()) }

    fragment { QrCodeScannerFragment() }
}