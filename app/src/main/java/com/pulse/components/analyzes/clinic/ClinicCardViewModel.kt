package com.pulse.components.analyzes.clinic

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.components.analyzes.clinic.repository.ClinicCardRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ClinicCardViewModel(private val repository: ClinicCardRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _branchesListLiveData by lazy { SingleLiveEvent<Int>() }
    val branchesListLiveData by lazy {
        _branchesListLiveData.switchMap {
            requestLiveData {
                repository.branchesList(it).items
            }
        }
    }
    private val _categoriesLiveData by lazy { SingleLiveEvent<Int>() }
    val categoriesLiveData by lazy {
        _branchesListLiveData.switchMap {
            requestLiveData {
                repository.categoriesList(it).items
            }
        }
    }

    fun fetchCategories(id: Int) {
        _categoriesLiveData.postValue(id)
    }

    fun fetchBranches(id: Int) {
        _branchesListLiveData.postValue(id)
    }


    fun selectCategory(category: AnalyzeCategory) {

    }
}