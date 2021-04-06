package com.pulse.components.categories

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.categories.adapter.CategoryAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentCategoriesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoriesFragment : BaseMVVMFragment<CategoriesViewModel>(R.layout.fragment_categories, CategoriesViewModel::class) {

    private val args by navArgs<CategoriesFragmentArgs>()
    private val binding by viewBinding(FragmentCategoriesBinding::bind)
    override val viewModel: CategoriesViewModel by viewModel { parametersOf(args.category) }
    private val clickAction by lazy { return@lazy viewModel::selectCategory }
    private val categoryAdapter by lazy { CategoryAdapter(clickAction) }
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen._2sdp) }

    override fun initUI() = with(binding) {
        attachBackPressCallback { viewModel.handleBackPress() }
        ivBack.setDebounceOnClickListener {
            hideKeyboard()
            viewModel.handleBackPress()
        }

        initCategoryList()
        viewSearch.setSearchListener { value ->
            viewLifecycleOwner.lifecycleScope.launch { categoryAdapter.filter { it.name?.contains(value, true).falseIfNull() } }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.viewSearch.setText("") // TODO review this case
    }

    private fun initCategoryList() = with(binding.rvCategories) {
        adapter = categoryAdapter
        addItemDecorator(true, spacing)
        setHasFixedSize(true)
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.navigateBackEvent.events) { navigationBack() }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.parentCategoriesState) { it?.let(categoryAdapter::notifyDataSet) }
        observe(viewModel.nestedCategoriesState) { it?.let(categoryAdapter::notifyDataSet) }
    }
}