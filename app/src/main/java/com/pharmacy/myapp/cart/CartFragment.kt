package com.pharmacy.myapp.cart

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import com.pharmacy.myapp.R
import com.pharmacy.myapp.cart.CartFragmentDirections.Companion.fromCartToCheckout
import com.pharmacy.myapp.cart.CartFragmentDirections.Companion.fromCartToSearch
import com.pharmacy.myapp.cart.adapter.CartAdapter
import com.pharmacy.myapp.cart.adapter.animator.ItemExpandAnimator
import com.pharmacy.myapp.cart.model.CartItem
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.showAlertRes
import com.pharmacy.myapp.core.extensions.visible
import com.pharmacy.myapp.core.extensions.visibleOrGone
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment(private val viewModel: CartViewModel) : BaseMVVMFragment(R.layout.fragment_cart) {

    private var concatAdapter: ConcatAdapter? = null
        set(value) {
            field = value
            rvCart.adapter = field
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCartProducts()
        showBackButton()

        with(rvCart) {
            setHasFixedSize(true)
            itemAnimator = ItemExpandAnimator()
        }

        ecvPharmacy.setButtonAction {
            navController.navigate(fromCartToSearch())
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.cartItemLiveData) { items ->
            val isListEmpty = items.isEmpty()

            if (!isListEmpty) {
                concatAdapter = ConcatAdapter(
                    ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build(),
                    items.map { CartAdapter(it, ::askConfirmation, ::startDeliveryProcess) { concatAdapter } })
            }

            ecvPharmacy.visibleOrGone(isListEmpty)
        }

        observe(viewModel.removeItemLiveData) { productId ->
            concatAdapter?.let {
                it.adapters.forEach { adapter ->
                    if (adapter is CartAdapter) {
                        adapter.notifyRemoveIfContains(productId) { nestedAdapter ->
                            concatAdapter?.removeAdapter(nestedAdapter)
                            if (it.adapters.isEmpty()) {
                                ecvPharmacy.visible()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun askConfirmation(productId: Int) = showAlertRes(getString(R.string.areYouSure)) {
        positive = R.string.delete
        positiveAction = { viewModel.removeProductFromCart(productId) }
        negative = R.string.cancel
    }

    private fun startDeliveryProcess(cartItem: CartItem) = navController.navigate(fromCartToCheckout(cartItem))
}