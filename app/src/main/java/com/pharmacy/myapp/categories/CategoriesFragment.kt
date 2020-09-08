package com.pharmacy.myapp.categories

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacy.myapp.R
import com.pharmacy.myapp.categories.adapter.CategoriesAdapter
import com.pharmacy.myapp.categories.adapter.NestedCategoriesAdapter
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addGridItemDecorator
import com.pharmacy.myapp.core.extensions.addItemDecorator
import com.pharmacy.myapp.core.extensions.onClick
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment(private val viewModel: CategoriesViewModel) : BaseMVVMFragment(R.layout.fragment_categories) {

    private val clickAction = viewModel::adapterClicked
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen._4sdp) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachBackPressCallback { viewModel.handleBackPress() }
        ivBackCategories.onClick { viewModel.handleBackPress() }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.directionLiveData, navController::navigate)

        observe(viewModel.navigateBackLiveData) { navigationBack() }
        observe(viewModel.parentCategoriesLiveData) {
            rvCategories.adapter = CategoriesAdapter(it.toMutableList(), clickAction)
            rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
            clearItemDecoration()
            rvCategories.addGridItemDecorator()
        }
        observe(viewModel.nestedCategoriesLiveData) {
            rvCategories.adapter = NestedCategoriesAdapter(it.toMutableList(), clickAction) // todo temp
            rvCategories.layoutManager = LinearLayoutManager(requireContext())
            clearItemDecoration()
            rvCategories.addItemDecorator(true, spacing, spacing, spacing, spacing)
        }
    }

    private fun clearItemDecoration() {
        if (rvCategories.itemDecorationCount > 0)
            rvCategories.removeItemDecorationAt(0)
    }

}