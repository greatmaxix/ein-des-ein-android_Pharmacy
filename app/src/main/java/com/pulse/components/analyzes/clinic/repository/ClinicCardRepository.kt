package com.pulse.components.analyzes.clinic.repository

import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class ClinicCardRepository(private val rds: ClinicCardRemoteDataSource, private val lds: ClinicCardLocalDataSource) : KoinComponent {

    fun branchesList(id: Int) = rds.branchesList(id)

    fun categoriesList(id: Int) = rds.categoriesList(id)
}