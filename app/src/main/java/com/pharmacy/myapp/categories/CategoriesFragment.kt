package com.pharmacy.myapp.categories

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacy.myapp.R
import com.pharmacy.myapp.categories.adapter.CategoriesAdapter
import com.pharmacy.myapp.categories.adapter.NestedCategoriesAdapter
import com.pharmacy.myapp.core.base.adapter.BaseFilterRecyclerAdapter
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addGridItemDecorator
import com.pharmacy.myapp.core.extensions.addItemDecorator
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.model.category.Category
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoriesFragment : BaseMVVMFragment(R.layout.fragment_categories) {

    private val args by navArgs<CategoriesFragmentArgs>()
    private val viewModel: CategoriesViewModel by viewModel { parametersOf(args.category) }

    private val clickAction by lazy { return@lazy viewModel::selectCategory }
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen._4sdp) }
    private var adapter: BaseFilterRecyclerAdapter<Category, *>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachBackPressCallback { viewModel.handleBackPress() }
        ivBackCategories.onClick { viewModel.handleBackPress() }

        searchViewCategories.setSearchListener { value ->
            viewLifecycleOwner.lifecycleScope.launch { adapter?.filter { it.name.contains(value, true) } }
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.directionLiveData, navController::navigate)

        observe(viewModel.navigateBackLiveData) { navigationBack() }
        observe(viewModel.parentCategoriesLiveData) {
            setAdapter(CategoriesAdapter(clickAction).apply { notifyDataSet(it.toMutableList()) })
            rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
            rvCategories.addGridItemDecorator()
        }
        observe(viewModel.nestedCategoriesLiveData) {
            setAdapter(NestedCategoriesAdapter(clickAction).apply { notifyDataSet(it.toMutableList()) })
            rvCategories.layoutManager = LinearLayoutManager(requireContext())
            rvCategories.addItemDecorator(true, spacing, spacing, spacing, spacing)
        }
    }

    private fun setAdapter(categoriesAdapter: BaseFilterRecyclerAdapter<Category, *>) {
        adapter = categoriesAdapter
        rvCategories.adapter = categoriesAdapter
        clearItemDecoration()
    }

    private fun clearItemDecoration() {
        if (rvCategories.itemDecorationCount > 0)
            rvCategories.removeItemDecorationAt(0)
    }

}