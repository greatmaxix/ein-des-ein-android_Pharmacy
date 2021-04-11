package com.pulse.components.analyzes.clinic.card

import androidx.lifecycle.viewModelScope
import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.components.analyzes.clinic.card.repository.ClinicCardRepository
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.StateEventFlow
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ClinicCardViewModel(private val repository: ClinicCardRepository) : BaseViewModel() {

    val branchesListState = StateEventFlow<List<Clinic>>(listOf())
    val categoriesListState = StateEventFlow<List<AnalyzeCategory>>(listOf())

    fun fetchBranches(id: Int) = viewModelScope.execute {
        branchesListState.postState(repository.branchesList(id).items)
    }

    fun fetchCategories(id: Int) = viewModelScope.execute {
        categoriesListState.postState(repository.categoriesList(id).items)
    }
}