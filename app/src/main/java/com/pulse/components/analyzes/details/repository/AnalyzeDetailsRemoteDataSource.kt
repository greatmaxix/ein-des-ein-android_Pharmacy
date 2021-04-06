package com.pulse.components.analyzes.details.repository

import com.pulse.data.remote.DummyData
import com.pulse.data.remote.api.RestApi
import com.pulse.model.PaginationModel

class AnalyzeDetailsRemoteDataSource(private val ra: RestApi) {

    fun clinicsList(code: String) = PaginationModel(1, DummyData.clinicsList, 20, 20) // ra.getClinicsList(code) TODO implement
}