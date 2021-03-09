package com.pulse.components.analyzes.clinic.repository

import com.pulse.data.remote.DummyData
import com.pulse.data.remote.api.RestApi
import com.pulse.model.PaginationModel

class ClinicTabsRemoteDataSource(private val ra: RestApi) {

    fun clinicsList() = PaginationModel(1, DummyData.clinicsList, 20, 20) // ra.getClinicsList(code)
}