package com.pulse.user.wishlist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pulse.R
import com.pulse.core.extensions.animateVisibleOrGone
import com.pulse.core.extensions.onNavDestinationSelected
import com.pulse.productList.BaseProductListFragment
import kotlinx.android.synthetic.main.fragment_wish.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class WishFragment(private val viewModel: WishViewModel) : BaseProductListFragment<WishViewModel>(R.layout.fragment_wish, viewModel) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()

        emptyContentWish.setButtonAction { navController.onNavDestinationSelected(R.id.nav_search, null, R.id.nav_wish) }

        productAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeRemoved(position: Int, count: Int) = setListVisibility(productAdapter.itemCount == 1)
            override fun onItemRangeInserted(position: Int, count: Int) = setListVisibility(count == 0)
        })
    }

    private fun setListVisibility(visible: Boolean) {
        emptyContentWish.animateVisibleOrGone(visible)
        rvProducts.animateVisibleOrGone(!visible)
    }

    override fun notifyWish(globalProductId: Int) = productAdapter.refresh()

    override val productLiveData
        get() = viewModel.wishLiveData
}