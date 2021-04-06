package com.pulse.components.cart

import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.cart.CartFragmentDirections.Companion.fromCartToCheckout
import com.pulse.components.cart.CartFragmentDirections.Companion.fromCartToSearch
import com.pulse.components.cart.adapter.CartAdapter
import com.pulse.components.cart.adapter.animator.ItemExpandAnimator
import com.pulse.components.cart.model.CartItem
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentCartBinding

class CartFragment : BaseToolbarFragment<CartViewModel>(R.layout.fragment_cart, CartViewModel::class) {

    private val binding by viewBinding(FragmentCartBinding::bind)
    private var concatAdapter = ConcatAdapter(concatWithIsolate)
        set(value) {
            field = value
            binding.rvCart.adapter = field
        }

    override fun initUI() = with(binding) {
        showBackButton()

        concatAdapter.clearAdapter()
        with(rvCart) {
            adapter = concatAdapter
            itemAnimator = ItemExpandAnimator()
        }

        viewEmpty.setButtonAction { navController.navigate(fromCartToSearch()) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateProducts()
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.errorEvent.events) {
            errorOrAuth(it.resId)
        }
        observe(viewModel.removeItemEvent.events) {
            removeProduct(it)
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.cartItemState) {
            concatAdapter.clearAdapter()
            buildCart(it)
        }
    }

    private fun buildCart(items: List<CartItem>) {
        val isListEmpty = items.isEmpty()

        if (!isListEmpty) {
            val listWithDivider = items.onEach { item -> item.products.last().needShowDivider = true }
            listWithDivider.forEach { concatAdapter.addAdapter(CartAdapter(it, ::askConfirmation, ::startDeliveryProcess)) }
        }

        binding.viewEmpty.visibleOrGone(isListEmpty)
    }

    private fun removeProduct(productId: Int) = with(concatAdapter) {
        adapters.forEach { adapter ->
            if (adapter is CartAdapter) {
                adapter.notifyRemoveIfContains(productId) { nestedAdapter ->
                    removeAdapter(nestedAdapter)
                    if (adapters.isEmpty()) {
                        binding.viewEmpty.visible()
                    }
                }
            }
        }
    }

    private fun errorOrAuth(@StringRes strResId: Int) {
        if (strResId == R.string.forCheckCart) {
            uiHelper.showDialog(getString(strResId)) {
                cancelable = false
                positive = getString(R.string.signIn)
                negative = getString(R.string.cancel)
                positiveAction = { navController.navigate(R.id.fromCartToAuth, SignInFragmentArgs(R.id.nav_cart).toBundle()) }
                negativeAction = { navController.onNavDestinationSelected(R.id.nav_home, inclusive = true) }
            }
        } else {
            uiHelper.showDialog(getString(strResId))
        }
    }

    private fun askConfirmation(productId: Int) = showAlertRes(getString(R.string.areYouSure)) {
        positive = R.string.delete
        positiveAction = { viewModel.removeProductFromCart(productId) }
        negative = R.string.cancel
    }

    private fun startDeliveryProcess(cartItem: CartItem) = navController.navigate(fromCartToCheckout(cartItem))
}