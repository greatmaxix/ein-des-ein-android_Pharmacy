package com.pharmacy.myapp.search

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.sign.SignInFragmentArgs
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.spanSearchCount
import com.pharmacy.myapp.core.extensions.visibleOrGone
import com.pharmacy.myapp.productList.BaseProductListFragment
import com.pharmacy.myapp.search.SearchFragmentDirections.Companion.fromSearchToScanner
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