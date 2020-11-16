package com.pulse.components.recipes

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.components.recipes.repository.RecipesPagingSource
import com.pulse.components.recipes.repository.RecipesRepository
import com.pulse.core.base.mvvm.BaseViewModel

class RecipesViewModel(private val repository: RecipesRepository) : BaseViewModel() {

    private val _recipesCountLiveData by lazy { MutableLiveData<Int>() }
    val recipesCountLiveData: LiveData<Int> by lazy { _recipesCountLiveData.distinctUntilChanged() }

    val recipesLiveData = Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { RecipesPagingSource(repository, _recipesCountLiveData::postValue) }
        .flow.cachedIn(viewModelScope)
        .asLiveData()

    companion object {
        private const val PAGE_SIZE = 20
        private const val INIT_LOAD_SIZE = PAGE_SIZE * 2
    }
}