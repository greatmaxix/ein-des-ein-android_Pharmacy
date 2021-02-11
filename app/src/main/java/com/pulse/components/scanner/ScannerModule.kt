package com.pulse.components.scanner

import com.pulse.components.scanner.repository.ScannerRemoteDataSource
import com.pulse.components.scanner.repository.ScannerRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val qrCodeScannerModule = module {

    viewModel { ScannerViewModel(get()) }

    fragment { ScannerFragment(get()) }
    fragment { ScannerListFragment(get()) }

    single { ScannerRepository(get(), get()) }
    single { ScannerRemoteDataSource(get()) }

}