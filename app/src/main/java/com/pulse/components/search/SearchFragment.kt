package com.pulse.components.search

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.productList.BaseProductListFragment
import com.pulse.components.search.SearchFragmentDirections.Companion.fromSearchToScanner
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.spanSearchCount
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.FragmentSearchBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SearchFragment(private val viewModel: SearchViewModel) : BaseProductListFragment<SearchViewModel>(R.layout.fragment_search, viewModel) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    override val productLiveData
        get() = viewModel.pagedSearchLiveData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        mcvScan.setDebounceOnClickListener { navController.navigate(fromSearchToScanner()) }
        viewSearch.setSearchListener { viewModel.doSearch(it.toString()) }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.productCountLiveData) {
            binding.mtvSearchResult.text = getString(R.string.countProducts, it).spanSearchCount(it)
            binding.viewEmpty.visibleOrGone(it == 0)
        }
    }

    override fun needToLogin() = navController.navigate(R.id.fromSearchToAuth, SignInFragmentArgs(R.id.nav_search).toBundle())
}