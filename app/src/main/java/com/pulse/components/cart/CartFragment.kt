package com.pulse.components.cart

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.ConcatAdapter
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.cart.CartFragmentDirections.Companion.fromCartToCheckout
import com.pulse.components.cart.CartFragmentDirections.Companion.fromCartToSearch
import com.pulse.components.cart.adapter.CartAdapter
import com.pulse.components.cart.adapter.animator.ItemExpandAnimator
import com.pulse.components.cart.model.CartItem
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment(private val vm: CartViewModel) : BaseMVVMFragment(R.layout.fragment_cart) {

    private var concatAdapter = ConcatAdapter(concatWithIsolate)
        set(value) {
            field = value
            rvCart.adapter = field
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()

        concatAdapter.clearAdapter()
        with(rvCart) {
            adapter = concatAdapter
            itemAnimator = ItemExpandAnimator()
        }

        ecvPharmacy.setButtonAction {
            navController.navigate(fromCartToSearch())
        }
    }

    override fun onResume() {
        super.onResume()
        observeResult(vm.cartItemLiveData) {
            onEmmit = { buildCart(this) }
            onError = { errorOrAuth(it.resId) }
        }
    }

    override fun onBindLiveData() {
        observeResult(vm.removeItemLiveData) {
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

    private fun removeProduct(productId: Int) = with(concatAdapter) {
        adapters.forEach { adapter ->
            if (adapter is CartAdapter) {
                adapter.notifyRemoveIfContains(productId) { nestedAdapter ->
                    removeAdapter(nestedAdapter)
                    if (adapters.isEmpty()) {
                        ecvPharmacy.visible()
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
        positiveAction = { vm.removeProductFromCart(productId) }
        negative = R.string.cancel
    }

    private fun startDeliveryProcess(cartItem: CartItem) = navController.navigate(fromCartToCheckout(cartItem))
}