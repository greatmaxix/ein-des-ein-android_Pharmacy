package com.pulse.components.analyzes.list

import androidx.lifecycle.viewModelScope
import com.pulse.components.analyzes.list.model.Analyze
import com.pulse.components.analyzes.list.repository.AnalyzesRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.StateEventFlow
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzesViewModel(private val repository: AnalyzesRepository) : BaseViewModel() {

    val analyzesListState = StateEventFlow<List<Analyze>>(listOf())

    init {
        viewModelScope.execute {
            analyzesListState.postState(repository.analyzesList().items)
        }
    }
}