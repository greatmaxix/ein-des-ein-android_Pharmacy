package com.pharmacy.myapp.search

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToProductCard
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.search.adapter.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.launch

class SearchFragment(private val viewModel: SearchViewModel) : BaseMVVMFragment(R.layout.fragment_search) {

    private val searchAdapter = SearchAdapter(viewModel::getProductInfo)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mcvScanSearch.onClick { navController.onNavDestinationSelected(R.id.globalToQrCodeScanner, null, R.id.nav_search) }

        searchView.setSearchListener { viewModel.doSearch(it.toString()) }

        with(rvProducts) {
            adapter = searchAdapter
            addAutoKeyboardCloser()
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.pagedSearchLiveData) {
            viewLifecycleOwner.lifecycleScope.launch {
                searchAdapter.submitData(it)
            }
        }

        observe(viewModel.productCountLiveData) {
            tvSearchResult.text = getString(R.string.countProducts, it)
            llDrugsNotFoundContainer.visibleOrGone(it == 0)
        }

        observe(viewModel.productLiteLiveData) {
            navController.navigate(globalToProductCard(it))
        }
    }
}