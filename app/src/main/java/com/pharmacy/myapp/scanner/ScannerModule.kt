package com.pharmacy.myapp.scanner

import com.pharmacy.myapp.scanner.repository.ScannerRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val qrCodeScannerModule = module {

    single { ScannerRepository(get(), get()) }

    viewModel { ScannerViewModel(get()) }

    fragment { ScannerFragment(get()) }
}