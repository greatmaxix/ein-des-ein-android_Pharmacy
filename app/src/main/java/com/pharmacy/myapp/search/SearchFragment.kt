package com.pharmacy.myapp.search

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToProductCard
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.produtcList.ProductListAdapter
import com.pharmacy.myapp.search.SearchFragmentDirections.Companion.fromSearchToAuth
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment(private val viewModel: SearchViewModel) : BaseMVVMFragment(R.layout.fragment_search) {

    private val productAdapter = ProductListAdapter(viewModel::getProductInfo, viewModel::setOrRemoveWish)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mcvScanSearch.onClick { navController.onNavDestinationSelected(R.id.globalToQrCodeScanner, null, R.id.nav_search) }

        searchView.setSearchListener { viewModel.doSearch(it.toString()) }

        with(rvProducts) {
            adapter = productAdapter
            addAutoKeyboardCloser()
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData, ::errorOrDialog)
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.pagedSearchLiveData) { productAdapter.submitData(lifecycle, it) }

        observe(viewModel.productCountLiveData) {
            tvSearchResult.text = getString(R.string.countProducts, it)
            llDrugsNotFoundContainer.visibleOrGone(it == 0)
        }

        observe(viewModel.productLiteLiveData) { navController.navigate(globalToProductCard(it)) }

        observe(viewModel.wishLiteLiveData, productAdapter::notifyWish)
    }

    private fun errorOrDialog(@StringRes strResId: Int) {
        if (strResId == R.string.forAddingProduct) {
            showAlert(strResId) {
                positive = getString(R.string.signIn)
                negative = getString(R.string.cancel)
                positiveAction = {
                    navController.onNavDestinationSelected(fromSearchToAuth())
                }
            }
        } else {
            messageCallback?.showError(strResId)
        }
    }
}