package com.pharmacy.myapp.user.wishlist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToProductCard
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.animateVisibleOrGone
import com.pharmacy.myapp.core.extensions.onNavDestinationSelected
import com.pharmacy.myapp.produtcList.ProductListAdapter
import kotlinx.android.synthetic.main.fragment_wish.*

class WishFragment(private val viewModel: WishViewModel) : BaseMVVMFragment(R.layout.fragment_wish) {

    private val productAdapter = ProductListAdapter(viewModel::getProductInfo, viewModel::setOrRemoveWish)

    private val observer = object : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeInserted(position: Int, count: Int) = setListVisibility(count == 0)

        override fun onItemRangeRemoved(position: Int, count: Int) = setListVisibility(productAdapter.itemCount == 1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        rvWish.adapter = productAdapter
        emptyContentWish.setButtonAction { navController.onNavDestinationSelected(R.id.nav_search, null, R.id.nav_wish) }

        productAdapter.registerAdapterDataObserver(observer)
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.wishLiveData) { productAdapter.submitData(lifecycle, it) }
        observe(viewModel.wishLiteLiveData) { productAdapter.refresh() }

        observe(viewModel.productLiteLiveData) { navController.navigate(globalToProductCard(it)) }
    }

    private fun setListVisibility(visible: Boolean) {
        emptyContentWish.animateVisibleOrGone(visible)
        rvWish.animateVisibleOrGone(!visible)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        productAdapter.unregisterAdapterDataObserver(observer)
    }

}