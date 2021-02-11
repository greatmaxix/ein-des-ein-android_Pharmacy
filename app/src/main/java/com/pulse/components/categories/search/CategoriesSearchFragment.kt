package com.pulse.components.categories.search

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.productList.BaseProductListFragment
import com.pulse.core.extensions.spanSearchCount
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.FragmentCategoriesSearchBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class CategoriesSearchFragment(private val viewModel: CategoriesSearchViewModel) :
    BaseProductListFragment<CategoriesSearchViewModel>(R.layout.fragment_categories_search, viewModel) {

    private val binding by viewBinding(FragmentCategoriesSearchBinding::bind)
    private val args by navArgs<CategoriesSearchFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        toolbar.toolbar.title = args.category.name

        viewModel.doCategorySearch(args.category.code)
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.productCountLiveData) {
            binding.mtvSearchResult.text = getString(R.string.countProducts, it).spanSearchCount(it)
            binding.llDrugsNotFound.visibleOrGone(it == 0)
        }
    }

    override fun needToLogin() = navController.navigate(R.id.fromSearchToAuth, SignInFragmentArgs(R.id.nav_search).toBundle())

    override fun notifyWish(globalProductId: Int) = productAdapter.notifyWish(globalProductId)

    override val productLiveData
        get() = viewModel.pagedSearchLiveData
}