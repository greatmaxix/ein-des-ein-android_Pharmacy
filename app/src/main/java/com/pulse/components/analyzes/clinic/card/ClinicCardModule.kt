package com.pulse.components.analyzes.clinic.card

import com.pulse.components.analyzes.clinic.card.repository.ClinicCardLocalDataSource
import com.pulse.components.analyzes.clinic.card.repository.ClinicCardRemoteDataSource
import com.pulse.components.analyzes.clinic.card.repository.ClinicCardRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val clinicCardModule = module {

    single { ClinicCardRepository(get(), get()) }
    single { ClinicCardRemoteDataSource(get()) }
    single { ClinicCardLocalDataSource() }

    viewModel { ClinicCardViewModel(get()) }

    fragment { ClinicCardFragment(get()) }
}