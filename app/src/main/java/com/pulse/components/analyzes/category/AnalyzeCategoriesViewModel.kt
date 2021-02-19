package com.pulse.components.analyzes.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.pulse.components.analyzes.category.AnalyzeCategoriesFragmentDirections.Companion.fromAnalyzeCategoriesToAnalyzeDetails
import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.components.analyzes.category.repository.AnalyzeCategoriesRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.general.SingleLiveEvent

class AnalyzeCategoriesViewModel(private val repository: AnalyzeCategoriesRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _parentCategoriesLiveData by lazy { SingleLiveEvent<List<AnalyzeCategory>>() }
    val parentCategoriesLiveData: LiveData<List<AnalyzeCategory>> by lazy { _parentCategoriesLiveData }

    private val _nestedCategoriesLiveData by lazy { MutableLiveData<List<AnalyzeCategory>>() }
    val nestedCategoriesLiveData: LiveData<List<AnalyzeCategory>> by lazy { _nestedCategoriesLiveData }

    private val _navigateBackLiveData by lazy { MutableLiveData<Unit>() }
    val navigateBackLiveData: LiveData<Unit> by lazy { _navigateBackLiveData }

    private val _selectedCategoryLiveData by lazy { MutableLiveData<AnalyzeCategory?>() }
    val selectedCategoryLiveData: LiveData<AnalyzeCategory?> by lazy { _selectedCategoryLiveData }

    private var originalList: List<AnalyzeCategory>? = null
    private var parentList: List<AnalyzeCategory>? = null
    private var selectedCategory: AnalyzeCategory? = null
        set(value) {
            field = value
            _selectedCategoryLiveData.postValue(value)
        }

    init {
        launchIO {
            originalList = repository.getAnalyzeCategories()
            parentList = originalList
            selectedCategory?.let {
                selectCategory(it)
            } ?: run {
                _parentCategoriesLiveData.postValue(parentList)
            }
        }
    }

    fun handleBackPress() {
        val category = selectedCategory ?: run {
            _navigateBackLiveData.postValue(Unit)
            return
        }
        if (category.topLevel) {
            _parentCategoriesLiveData.postValue(parentList)
            selectedCategory = null
        } else {
            val parent = findParentList1(originalList)
            selectedCategory = parent
            _nestedCategoriesLiveData.postValue(parent?.nodes)
        }
    }

    fun selectCategory(category: AnalyzeCategory) {
        if (category.nodes?.isNotEmpty().falseIfNull()) {
            selectedCategory = category
            _nestedCategoriesLiveData.postValue(category.nodes)
        } else {
            _directionLiveData.postValue(fromAnalyzeCategoriesToAnalyzeDetails(category))
        }
    }

    private fun findParentList1(list: List<AnalyzeCategory>?): AnalyzeCategory? {
        var parentCategory: AnalyzeCategory? = null
        list?.forEach { category ->
            if (category.nodes?.contains(selectedCategory).falseIfNull()) {
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