package com.pulse.components.analyzes.list.repository

import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class AnalyzesRepository(private val rds: AnalyzesRemoteDataSource, private val lds: AnalyzesLocalDataSource) : KoinComponent {

}