package com.pulse.components.analyzes.clinic.card.repository

import com.pulse.data.remote.DummyData
import com.pulse.data.remote.api.RestApi
import com.pulse.model.PaginationModel

class ClinicCardRemoteDataSource(private val ra: RestApi) {

    fun branchesList(id: Int) = PaginationModel(1, DummyData.clinicsList, 20, 20) // TODO ra.getClinicsList(id)

    fun categoriesList(id: Int) = PaginationModel(1, DummyData.analyzeCategories, 20, 20) // TODO ra.getAnalyzeCategories(id)
}