package com.pulse.components.analyzes.category

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.category.adapter.AnalyzeCategoryAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentAnalyzeCategoriesBinding
import kotlinx.coroutines.launch

class AnalyzeCategoriesFragment(private val viewModel: AnalyzeCategoriesViewModel) : BaseMVVMFragment(R.layout.fragment_analyze_categories) {

    private val binding by viewBinding(FragmentAnalyzeCategoriesBinding::bind)
    private val clickAction by lazy { return@lazy viewModel::selectCategory }
    private val analyzeCategoryAdapter by lazy { AnalyzeCategoryAdapter(clickAction) }
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen._2sdp) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton { viewModel.handleBackPress() }
        attachBackPressCallback { viewModel.handleBackPress() }
        ivBack.setDebounceOnClickListener { viewModel.handleBackPress() }

        initCategoryList()
        viewSearch.setSearchListener { value ->
            viewLifecycleOwner.lifecycleScope.launch { analyzeCategoryAdapter.filter { it.name?.contains(value, true).falseIfNull() } }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.viewSearch.setText("") // TODO review this case
    }

    private fun initCategoryList() = with(binding.rvAnalyzeCategories) {
        adapter = analyzeCategoryAdapter
        addItemDecorator(true, spacing)
        setHasFixedSize(true)
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.navigateBackLiveData) { navigationBack() }
        observe(viewModel.parentCategoriesLiveData, analyzeCategoryAdapter::notifyDataSet)
        observe(viewModel.nestedCategoriesLiveData, analyzeCategoryAdapter::notifyDataSet)
        observeNullable(viewModel.selectedCategoryLiveData) { category ->
            category?.name?.let {
                toolbar?.title = it
                binding.llToolbar.gone()
                toolbar?.visible()
            } ?: run {
                toolbar?.gone()
                binding.llToolbar.visible()
            }
        }
    }
}