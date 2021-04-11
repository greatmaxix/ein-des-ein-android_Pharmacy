package com.pulse.components.categories.search

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.productList.BaseProductListFragment
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.spanSearchCount
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.FragmentCategoriesSearchBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinApiExtension

@ExperimentalCoroutinesApi
@KoinApiExtension
class CategoriesSearchFragment : BaseProductListFragment<CategoriesSearchViewModel>(R.layout.fragment_categories_search, CategoriesSearchViewModel::class) {

    private val binding by viewBinding(FragmentCategoriesSearchBinding::bind)
    private val args by navArgs<CategoriesSearchFragmentArgs>()
    override val pagedSearchState
        get() = viewModel.pagedSearchState

    override fun initUI() {
        with(binding) {
            showBackButton()
            toolbar.toolbar.title = args.category.name
            viewModel.doCategorySearch(args.category.code)
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        super.onBindStates()
        observe(viewModel.productCountState) {
            binding.mtvSearchResult.text = getString(R.string.countProducts, it).spanSearchCount(it)
            binding.llDrugsNotFound.visibleOrGone(it == 0)
        }
    }

    override fun needToLogin() = navController.navigate(R.id.fromSearchToAuth, SignInFragmentArgs(R.id.nav_search).toBundle())

    override fun notifyWish(globalProductId: Int) = productAdapter.notifyWish(globalProductId)
}