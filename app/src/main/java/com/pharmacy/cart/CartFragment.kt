package com.pharmacy.cart

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.ConcatAdapter
import com.pharmacy.R
import com.pharmacy.auth.sign.SignInFragmentArgs
import com.pharmacy.cart.CartFragmentDirections.Companion.fromCartToCheckout
import com.pharmacy.cart.CartFragmentDirections.Companion.fromCartToSearch
import com.pharmacy.cart.adapter.CartAdapter
import com.pharmacy.cart.adapter.animator.ItemExpandAnimator
import com.pharmacy.cart.model.CartItem
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.*
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment(private val viewModel: CartViewModel) : BaseMVVMFragment(R.layout.fragment_cart) {

    private var concatAdapter = ConcatAdapter(concatWithIsolate)
        set(value) {
            field = value
            rvCart.adapter = field
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cartOrAuth()

        showBackButton()

        with(rvCart) {
            clearAdapter()
            adapter = concatAdapter
            itemAnimator = ItemExpandAnimator()
        }

        ecvPharmacy.setButtonAction {
            navController.navigate(fromCartToSearch())
        }
    }

    //TODO move to extensions
    private fun clearAdapter() = concatAdapter.adapters.forEach(concatAdapter::removeAdapter)

    override fun onBindLiveData() {
        observeResult(viewModel.cartItemLiveData) {
            onEmmit = { buildCart(this) }
            onError = { errorOrAuth(it.resId) }
        }
        observeResult(viewModel.removeItemLiveData) {
            onEmmit = { removeProduct(this) }
        }
    }

    private fun buildCart(items: List<CartItem>) {
        val isListEmpty = items.isEmpty()

        if (!isListEmpty) {
            items.forEach { concatAdapter.addAdapter(CartAdapter(it, ::askConfirmation, ::startDeliveryProcess)) }
        }

        ecvPharmacy.visibleOrGone(isListEmpty)
    }

    private fun removeProduct(productId: Int) = concatAdapter.adapters.forEach { adapter ->
        if (adapter is CartAdapter) {
            adapter.notifyRemoveIfContains(productId) { nestedAdapter ->
                concatAdapter.removeAdapter(nestedAdapter)
                if (concatAdapter.adapters.isEmpty()) {
                    ecvPharmacy.visible()
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