package com.pharmacy.myapp.user.wishlist

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToProductCard
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.produtcList.ProductListAdapter
import kotlinx.android.synthetic.main.fragment_wish.*

class WishFragment(private val viewModel: WishViewModel) : BaseMVVMFragment(R.layout.fragment_wish) {

    private val productAdapter = ProductListAdapter(viewModel::getProductInfo, viewModel::setOrRemoveWish)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        rvWish.adapter = productAdapter
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.wishLiveData) { productAdapter.submitData(lifecycle, it) }
        observe(viewModel.wishLiteLiveData) { productAdapter.refresh() }

        observe(viewModel.productLiteLiveData) { navController.navigate(globalToProductCard(it)) }
    }
}