package com.pulse.components.cart

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.ConcatAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.cart.CartFragmentDirections.Companion.fromCartToCheckout
import com.pulse.components.cart.CartFragmentDirections.Companion.fromCartToSearch
import com.pulse.components.cart.adapter.CartAdapter
import com.pulse.components.cart.adapter.animator.ItemExpandAnimator
import com.pulse.components.cart.model.CartItem
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentCartBinding

class CartFragment(private val viewModel: CartViewModel) : BaseMVVMFragment(R.layout.fragment_cart) {

    private val binding by viewBinding(FragmentCartBinding::bind)
    private var concatAdapter = ConcatAdapter(concatWithIsolate)
        set(value) {
            field = value
            binding.rvCart.adapter = field
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
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
        observeResult(viewModel.cartItemLiveData) {
            onEmmit = {
                concatAdapter.clearAdapter()
                buildCart(this)
            }
            onError = { errorOrAuth(it.resId) }
        }
    }

    override fun onBindLiveData() {
        observeResult(viewModel.removeItemLiveData) {
            onEmmit = { removeProduct(this) }
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
            showAlert(strResId) {
                cancelable = false
                positive = getString(R.string.signIn)
                negative = getString(R.string.cancel)
                positiveAction = { navController.navigate(R.id.fromCartToAuth, SignInFragmentArgs(R.id.nav_cart).toBundle()) }
                negativeAction = { navController.onNavDestinationSelected(R.id.nav_home, inclusive = true) }
            }
        } else {
            messageCallback?.showError(strResId)
        }
    }

    private fun askConfirmation(productId: Int) = showAlertRes(getString(R.string.areYouSure)) {
        positive = R.string.delete
        positiveAction = { viewModel.removeProductFromCart(productId) }
        negative = R.string.cancel
    }

    private fun startDeliveryProcess(cartItem: CartItem) = navController.navigate(fromCartToCheckout(cartItem))
}