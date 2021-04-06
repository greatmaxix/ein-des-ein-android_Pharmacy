package com.pulse.components.analyzes.clinic.tabs

import androidx.lifecycle.viewModelScope
import com.pulse.components.analyzes.clinic.repository.ClinicTabsRepository
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ClinicTabsViewModel(private val repository: ClinicTabsRepository) : BaseViewModel() {

    val clinicsListState = StateEventFlow<List<Clinic>>(listOf()).apply {
        viewModelScope.execute { postState(repository.clinicsList().items) }
    }
    val selectedClinicState = SingleShotEvent<Clinic?>()
    val clinicEvent = SingleShotEvent<Clinic?>()

    fun getClinic(id: Int) = clinicEvent.offerEvent(clinicsListState.value.find { it.id == id })

    fun orderService(clinic: Clinic) = selectedClinicState.offerEvent(clinic)
}