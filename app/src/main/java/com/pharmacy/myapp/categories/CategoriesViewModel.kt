package com.pharmacy.myapp.categories

import androidx.core.text.trimmedLength
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.categories.CategoriesFragmentDirections.Companion.globalToSearchWithCategory
import com.pharmacy.myapp.categories.repository.CategoriesRepository
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.extensions.falseIfNull
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.model.category.Category

class CategoriesViewModel(private val repository: CategoriesRepository, private var selectedCategory: Category?) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _parentCategoriesLiveData by lazy { SingleLiveEvent<List<Category>>() }
    val parentCategoriesLiveData: LiveData<List<Category>> by lazy { _parentCategoriesLiveData }

    private val _nestedCategoriesLiveData by lazy { MutableLiveData<List<Category>>() }
    val nestedCategoriesLiveData: LiveData<List<Category>> by lazy { _nestedCategoriesLiveData }

    private val _navigateBackLiveData by lazy { MutableLiveData<Unit>() }
    val navigateBackLiveData: LiveData<Unit> by lazy { _navigateBackLiveData }

    private var originalList: List<Category>? = null
    private var parentList: List<Category>? = null

    init {
        launchIO {
            originalList = repository.getLocalCategories()
            parentList = originalList?.filter { it.code.trimmedLength() == 1 }
            selectedCategory?.let {
                selectCategory(it)
            } ?: run {
                _parentCategoriesLiveData.postValue(parentList)
            }
        }
    }

    fun handleBackPress() {
        val code = selectedCategory?.code ?: run {
            _navigateBackLiveData.postValue(Unit)
            return
        }
        if (code.trimmedLength() == 1) {
            _parentCategoriesLiveData.postValue(parentList)
            selectedCategory = null
        } else {
            _nestedCategoriesLiveData.postValue(findParentCategories())
        }
    }

    fun selectCategory(category: Category) {
        category.apply {
            nodes = originalList?.filter { category.nestedCategories?.contains(it.code).falseIfNull() }
        }
        if (category.nodes?.isNotEmpty().falseIfNull()) {
            selectedCategory = category
            _nestedCategoriesLiveData.postValue(category.nodes)
        } else {
            _directionLiveData.postValue(globalToSearchWithCategory(category))
        }
    }

    private fun findParentCategories(): List<Category>? {
        val find = originalList?.find { it.nestedCategories?.contains(selectedCategory?.code).falseIfNull() }
        selectedCategory = find
        return originalList?.filter { find?.nestedCategories?.contains(it.code).falseIfNull() }
    }

}