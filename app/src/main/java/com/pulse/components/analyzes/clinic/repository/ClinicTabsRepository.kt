package com.pulse.components.analyzes.clinic.repository

import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class ClinicTabsRepository(private val rds: ClinicTabsRemoteDataSource, private val lds: ClinicTabsLocalDataSource) : KoinComponent {

    fun clinicsList() = rds.clinicsList()
}