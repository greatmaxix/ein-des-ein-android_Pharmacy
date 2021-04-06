package com.pulse.components.scanner

import com.pulse.components.scanner.repository.ScannerRemoteDataSource
import com.pulse.components.scanner.repository.ScannerRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val qrCodeScannerModule = module {

    single { ScannerRepository(get(), get()) }
    single { ScannerRemoteDataSource(get()) }

    viewModel { ScannerViewModel(get()) }
}