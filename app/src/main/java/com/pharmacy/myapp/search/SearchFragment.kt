package com.pharmacy.myapp.search

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentArgs
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addAutoKeyboardCloser
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.showAlert
import com.pharmacy.myapp.core.extensions.visibleOrGone
import com.pharmacy.myapp.produtcList.ProductListAdapter
import com.pharmacy.myapp.search.SearchFragmentDirections.Companion.fromSearchToProduct
import com.pharmacy.myapp.search.SearchFragmentDirections.Companion.fromSearchToScanner
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment(private val viewModel: SearchViewModel) : BaseMVVMFragment(R.layout.fragment_search) {

    private val productAdapter = ProductListAdapter(viewModel::getProductInfo, viewModel::setOrRemoveWish)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mcvScanSearch.onClick { navController.navigate(fromSearchToScanner()) }

        searchView.setSearchListener { viewModel.doSearch(it.toString()) }

        with(rvProducts) {
            adapter = productAdapter
            addAutoKeyboardCloser()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkIsWishSaved()
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData, ::errorOrDialog)
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.pagedSearchLiveData) { productAdapter.submitData(lifecycle, it) }

        observe(viewModel.productCountLiveData) {
            tvSearchResult.text = getString(R.string.countProducts, it)
            llDrugsNotFoundContainer.visibleOrGone(it == 0)
        }

        observe(viewModel.productLiteLiveData) { navController.navigate(fromSearchToProduct(it)) }

        observe(viewModel.wishLiteLiveData) {
            viewModel.clearWishToSave()
            productAdapter.notifyWish(it)
        }
    }

    private fun errorOrDialog(@StringRes strResId: Int) {
        if (strResId == R.string.forAddingProduct) {
            showAlert(strResId) {
                positive = getString(R.string.signIn)
                negative = getString(R.string.cancel)
                positiveAction = {
                    navController.navigate(R.id.fromSearchToAuth, SignInFragmentArgs(R.id.nav_search).toBundle())
                }
            }
        } else {
            messageCallback?.showError(strResId)
        }
    }
}