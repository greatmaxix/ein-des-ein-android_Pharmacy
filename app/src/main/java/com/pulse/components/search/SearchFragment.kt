package com.pulse.components.search

import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.product.model.ProductLite
import com.pulse.components.productList.BaseProductListFragment
import com.pulse.components.search.SearchFragmentDirections.Companion.fromSearchToScanner
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.spanSearchCount
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.FragmentSearchBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinApiExtension

@ExperimentalCoroutinesApi
@KoinApiExtension
class SearchFragment : BaseProductListFragment<SearchViewModel>(R.layout.fragment_search, SearchViewModel::class) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    override val pagedSearchState: Flow<PagingData<ProductLite>>
        get() = viewModel.pagedSearchFlow

    override fun initUI() = with(binding) {
        mcvScan.setDebounceOnClickListener { navController.navigate(fromSearchToScanner()) }
        viewSearch.setSearchListener { viewModel.doSearch(it.toString()) }
    }

    override fun onBindStates() = with(lifecycleScope) {
        super.onBindStates()
        observe(viewModel.productCountState) {
            binding.mtvSearchResult.text = getString(R.string.countProducts, it).spanSearchCount(it)
            binding.viewEmpty.visibleOrGone(it== 0)
        }
    }

    override fun needToLogin() = navController.navigate(R.id.fromSearchToAuth, SignInFragmentArgs(R.id.nav_search).toBundle())
}