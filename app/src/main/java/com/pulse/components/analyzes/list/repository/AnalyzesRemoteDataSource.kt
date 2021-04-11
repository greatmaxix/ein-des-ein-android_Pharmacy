package com.pulse.components.analyzes.list.repository

import com.pulse.data.remote.DummyData
import com.pulse.data.remote.api.RestApi
import com.pulse.model.PaginationModel

class AnalyzesRemoteDataSource(private val ra: RestApi) {

    fun analyzesList() = PaginationModel(1, DummyData.analyzesList, 20, 20) // ra.getClinicsList(code) TODO implement
}