package com.pulse.components.analyzes.details.repository

import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class AnalyzeDetailsRepository(private val rds: AnalyzeDetailsRemoteDataSource, private val lds: AnalyzeDetailsLocalDataSource) : KoinComponent {

    fun clinicsList(code: String) = rds.clinicsList(code)
}