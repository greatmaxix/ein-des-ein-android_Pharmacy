package com.pulse.components.analyzes.clinic.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.pulse.components.analyzes.clinic.repository.ClinicTabsRepository
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ClinicTabsViewModel(private val repository: ClinicTabsRepository) : BaseViewModel() {

    private val _selectedClinicLiveData by lazy { SingleLiveEvent<Clinic>() }
    val selectedClinicLiveData: LiveData<Clinic> by lazy { _selectedClinicLiveData }

    private var clinics: List<Clinic> = listOf()
    val clinicsListLiveData by lazy {
        requestLiveData {
            clinics = repository.clinicsList().items
            clinics
        }
    }

    private val _clinicLiveData by lazy { SingleLiveEvent<Int>() }
    val clinicLiveData by lazy { _clinicLiveData.switchMap { id -> MutableLiveData(clinics.find { it.id == id }) } }

    fun getClinic(id: Int) = _clinicLiveData.postValue(id)

    fun orderService(clinic: Clinic) = _selectedClinicLiveData.postValue(clinic)
}