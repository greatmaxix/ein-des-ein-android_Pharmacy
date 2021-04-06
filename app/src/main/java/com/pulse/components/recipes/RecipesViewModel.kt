package com.pulse.components.recipes

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.components.recipes.repository.RecipesPagingSource
import com.pulse.components.recipes.repository.RecipesUseCase
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.data.GeneralException

class RecipesViewModel(private val useCase: RecipesUseCase) : BaseViewModel() {

    val recipesCountState = StateEventFlow(0)
    val recipesErrorEvent = SingleShotEvent<GeneralException>()

    val recipesFlow
        get() = Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) {
            RecipesPagingSource(useCase, recipesCountState::postState, recipesErrorEvent::offerEvent)
        }
            .flow
            .cachedIn(viewModelScope)

    companion object {

        private const val PAGE_SIZE = 20
        private const val INIT_LOAD_SIZE = PAGE_SIZE * 2
    }
}