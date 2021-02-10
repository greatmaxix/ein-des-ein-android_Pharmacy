package com.pulse.components.user.wishlist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.productList.BaseProductListFragment
import com.pulse.core.extensions.animateVisibleOrGone
import com.pulse.core.extensions.onNavDestinationSelected
import com.pulse.databinding.FragmentWishBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class WishFragment(private val viewModel: WishViewModel) : BaseProductListFragment<WishViewModel>(R.layout.fragment_wish, viewModel) {

    private val binding by viewBinding(FragmentWishBinding::bind)
    override val productLiveData
        get() = viewModel.wishLiveData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()

        viewEmptyContent.setButtonAction { navController.onNavDestinationSelected(R.id.nav_search, null, R.id.nav_wish) }

        productAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeRemoved(position: Int, count: Int) = setListVisibility(productAdapter.itemCount == 1)
            override fun onItemRangeInserted(position: Int, count: Int) = setListVisibility(count == 0)
        })
    }

    private fun setListVisibility(visible: Boolean) {
        binding.viewEmptyContent.animateVisibleOrGone(visible)
        binding.rvProducts.animateVisibleOrGone(!visible)
    }

    override fun notifyWish(globalProductId: Int) = productAdapter.refresh()
}