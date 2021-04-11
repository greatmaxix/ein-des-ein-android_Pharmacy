package com.pulse.components.analyzes.details

import androidx.lifecycle.viewModelScope
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.components.analyzes.details.repository.AnalyzeDetailsRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.StateEventFlow
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzeDetailsViewModel(private val repository: AnalyzeDetailsRepository) : BaseViewModel() {

    val clinicsListState = StateEventFlow<List<Clinic>>(listOf())

    fun requestClinics(code: String) = viewModelScope.execute {
        clinicsListState.postState(repository.clinicsList(code).items)
    }
}