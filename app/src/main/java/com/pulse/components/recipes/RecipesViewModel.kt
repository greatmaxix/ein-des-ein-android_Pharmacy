package com.pulse.components.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.components.recipes.repository.RecipesPagingSource
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.Event
import com.pulse.data.GeneralException

class RecipesViewModel(private val useCase: RecipesUseCase) : BaseViewModel() {

    private val _recipesCountLiveData by lazy { MutableLiveData<Int>() }
    val recipesCountLiveData: LiveData<Int> by lazy { _recipesCountLiveData }

    private val _recipesErrorLiveData by lazy { MutableLiveData<Event<GeneralException>>() }
    val recipesErrorLiveData: LiveData<Event<GeneralException>> by lazy { _recipesErrorLiveData }

    val recipesLiveData
        get() = Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) {
            RecipesPagingSource(useCase, _recipesCountLiveData::postValue, _recipesErrorLiveData::postValue)
        }
            .flow
            .cachedIn(viewModelScope)
            .asLiveData()

    companion object {
        private const val PAGE_SIZE = 20
        private const val INIT_LOAD_SIZE = PAGE_SIZE * 2
    }
}