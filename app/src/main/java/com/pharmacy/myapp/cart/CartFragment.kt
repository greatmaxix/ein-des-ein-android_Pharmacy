package com.pharmacy.myapp.cart

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.ConcatAdapter
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.AuthSignInFragmentArgs
import com.pharmacy.myapp.cart.CartFragmentDirections.Companion.fromCartToCheckout
import com.pharmacy.myapp.cart.CartFragmentDirections.Companion.fromCartToSearch
import com.pharmacy.myapp.cart.adapter.CartAdapter
import com.pharmacy.myapp.cart.adapter.animator.ItemExpandAnimator
import com.pharmacy.myapp.cart.model.CartItem
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
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
        observe(viewModel.errorLiveData, ::errorOrAuth)
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.cartItemLiveData, ::buildCart)
        observe(viewModel.removeItemLiveData, ::removeProduct)
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
                positiveAction = { navController.navigate(R.id.fromCartToAuth, AuthSignInFragmentArgs(R.id.nav_cart).toBundle()) }
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