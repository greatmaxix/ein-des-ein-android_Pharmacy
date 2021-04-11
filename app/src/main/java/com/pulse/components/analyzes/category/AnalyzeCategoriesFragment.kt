package com.pulse.components.analyzes.category

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.category.adapter.AnalyzeCategoryAdapter
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentAnalyzeCategoriesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AnalyzeCategoriesFragment : BaseToolbarFragment<AnalyzeCategoriesViewModel>(R.layout.fragment_analyze_categories, AnalyzeCategoriesViewModel::class) {

    private val args by navArgs<AnalyzeCategoriesFragmentArgs>()
    override val viewModel: AnalyzeCategoriesViewModel by viewModel(parameters = { parametersOf(args) })
    private val binding by viewBinding(FragmentAnalyzeCategoriesBinding::bind)
    private val analyzeCategoryAdapter by lazy { AnalyzeCategoryAdapter(viewModel::selectCategory) }
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen._2sdp) }

    override fun initUI() = with(binding) {
        showBackButton()
        attachBackPressCallback { viewModel.handleBackPress() }

        initCategoryList()
        viewSearch.setSearchListener { value ->
            viewLifecycleOwner.lifecycleScope.launch { analyzeCategoryAdapter.filter { it.name?.contains(value, true).falseIfNull() } }
        }
    }

    override fun onClickNavigation() {
        viewModel.handleBackPress()
    }

    override fun onResume() {
        super.onResume()
        binding.viewSearch.setText("") // TODO review this case
    }

    private fun initCategoryList() = with(binding.rvAnalyzeCategories) {
        analyzeCategoryAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() = updateListEmpty()
            override fun onItemRangeRemoved(position: Int, count: Int) = updateListEmpty()
            override fun onItemRangeInserted(position: Int, count: Int) = updateListEmpty()
        })
        adapter = analyzeCategoryAdapter
        addItemDecorator(true, spacing)
        setHasFixedSize(true)
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.navigateBackEvent.events) { navigationBack() }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.parentCategoriesState, analyzeCategoryAdapter::notifyDataSet)
        observe(viewModel.nestedCategoriesState) { it?.let(analyzeCategoryAdapter::notifyDataSet) }
        observe(viewModel.selectedCategoryState) { category ->
            category?.name?.let {
                setTitle(it)
                binding.viewSearch.gone()
            } ?: run {
                binding.viewSearch.visible()
                setTitle(getString(R.string.analyzesLabel))
            }
        }
    }

    private fun updateListEmpty() = with(binding) {
        val isEmpty = analyzeCategoryAdapter.itemCount == 0
        viewEmptyContent.visibleOrGone(isEmpty)
        rvAnalyzeCategories.visibleOrGone(!isEmpty)
    }
}