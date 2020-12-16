package com.pulse.categories

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.pulse.R
import com.pulse.categories.adapter.CategoryAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoriesFragment : BaseMVVMFragment(R.layout.fragment_categories) {

    private val args by navArgs<CategoriesFragmentArgs>()
    private val viewModel: CategoriesViewModel by viewModel { parametersOf(args.category) }
    private val clickAction by lazy { return@lazy viewModel::selectCategory }
    private val categoryAdapter by lazy { CategoryAdapter(clickAction) }
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen._2sdp) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachBackPressCallback { viewModel.handleBackPress() }
        ivBackCategories.setDebounceOnClickListener { viewModel.handleBackPress() }

        initCategoryList()
        searchViewCategories.setSearchListener { value ->
            viewLifecycleOwner.lifecycleScope.launch { categoryAdapter.filter { it.name?.contains(value, true).falseIfNull() } }
        }
    }

    override fun onResume() {
        super.onResume()
        searchViewCategories.setText("") // TODO review this case
    }

    private fun initCategoryList() = with(rvCategories) {
        adapter = categoryAdapter
        addItemDecorator(true, spacing)
        setHasFixedSize(true)
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.navigateBackLiveData) { navigationBack() }
        observe(viewModel.parentCategoriesLiveData) { categoryAdapter.notifyDataSet(it.toMutableList()) }
        observe(viewModel.nestedCategoriesLiveData) { categoryAdapter.notifyDataSet(it.toMutableList()) }
    }
}