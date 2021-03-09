package com.pulse.components.analyzes.checkout.repository

import com.pulse.data.remote.DummyData
import com.pulse.data.remote.api.RestApi
import com.pulse.model.PaginationModel

class AnalyzeCheckoutRemoteDataSource(private val ra: RestApi) {

    fun clinicsList(code: String) = PaginationModel(1, DummyData.clinicsList, 20, 20) // ra.getClinicsList(code)
}