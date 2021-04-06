package com.pulse.components.analyzes.category

import androidx.lifecycle.viewModelScope
import com.pulse.components.analyzes.category.AnalyzeCategoriesFragmentDirections.Companion.fromAnalyzeCategoriesToAnalyzeDetails
import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.components.analyzes.category.repository.AnalyzeCategoriesRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow

class AnalyzeCategoriesViewModel(private val args: AnalyzeCategoriesFragmentArgs, private val repository: AnalyzeCategoriesRepository) : BaseViewModel() {

    val parentCategoriesState = StateEventFlow<List<AnalyzeCategory>>(listOf())
    val nestedCategoriesState = StateEventFlow<List<AnalyzeCategory>?>(null)
    val selectedCategoryState = StateEventFlow<AnalyzeCategory?>(null)
    val navigateBackEvent = SingleShotEvent<Unit>()

    private var originalList: List<AnalyzeCategory> = listOf()
    private var parentList: List<AnalyzeCategory> = listOf()

    init {
        viewModelScope.execute {
            originalList = repository.getAnalyzeCategories(args.clinic?.id)
            parentList = originalList
            if (args.category != null) selectedCategoryState.postState(args.category)
            selectedCategoryState.value?.let {
                selectCategory(it)
            } ?: run {
                parentCategoriesState.postState(parentList)
            }
        }
    }

    fun handleBackPress() {
        val category = selectedCategoryState.value ?: run {
            navigateBackEvent.offerEvent(Unit)
            return
        }
        if (category.topLevel) {
            parentCategoriesState.postState(parentList)
            selectedCategoryState.postState(null)
        } else {
            val parent = findParentList1(originalList)
            selectedCategoryState.postState(parent)
            nestedCategoriesState.postState(parent?.nodes)
        }
    }

    fun selectCategory(category: AnalyzeCategory) {
        if (category.nodes?.isNotEmpty().falseIfNull()) {
            selectedCategoryState.postState(category)
            nestedCategoriesState.postState(category.nodes)
        } else {
            navEvent.postEvent(fromAnalyzeCategoriesToAnalyzeDetails(category, args.clinic))
        }
    }

    private fun findParentList1(list: List<AnalyzeCategory>?): AnalyzeCategory? {
        var parentCategory: AnalyzeCategory? = null
        list?.forEach { category ->
            if (category.nodes?.contains(selectedCategoryState.value).falseIfNull()) {
                parentCategory = category
                return@forEach
            } else {
                val find = findParentList1(category.nodes)
                if (find != null) {
                    parentCategory = find
                    return@forEach
                }
            }
        }

        return parentCategory
    }
}