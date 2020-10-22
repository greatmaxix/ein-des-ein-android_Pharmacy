package com.pulse.search

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.core.extensions.onClick
import com.pulse.core.extensions.spanSearchCount
import com.pulse.core.extensions.visibleOrGone
import com.pulse.productList.BaseProductListFragment
import com.pulse.search.SearchFragmentDirections.Companion.fromSearchToScanner
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SearchFragment(private val viewModel: SearchViewModel) : BaseProductListFragment<SearchViewModel>(R.layout.fragment_search, viewModel) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mcvScanSearch.onClick { navController.navigate(fromSearchToScanner()) }

        searchView.setSearchListener { viewModel.doSearch(it.toString()) }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.productCountLiveData) {
            tvSearchResult.text = getString(R.string.countProducts, it).spanSearchCount(it)
            ecvPharmacy.visibleOrGone(it == 0)
        }
    }

    override fun needToLogin() = navController.navigate(R.id.fromSearchToAuth, SignInFragmentArgs(R.id.nav_search).toBundle())

    override val productLiveData
        get() = viewModel.pagedSearchLiveData
}