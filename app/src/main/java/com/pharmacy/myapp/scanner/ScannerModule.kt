package com.pharmacy.myapp.scanner

import com.pharmacy.myapp.scanner.repository.ScannerRemoteDataSource
import com.pharmacy.myapp.scanner.repository.ScannerRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val qrCodeScannerModule = module {

    viewModel { ScannerViewModel(get()) }

    fragment { ScannerFragment(get()) }
    fragment { ScannerListFragment(get()) }

    single { ScannerRepository(get(), get()) }
    single { ScannerRemoteDataSource(get()) }

}