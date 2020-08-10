package com.pharmacy.myapp.checkout

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.CheckoutFragmentDirections.Companion.actionCheckoutToPromoCodeDialog
import com.pharmacy.myapp.checkout.adapter.OrderProductsAdapter
import com.pharmacy.myapp.checkout.dialog.PromoCodeDialogFragment
import com.pharmacy.myapp.checkout.dialog.PromoCodeDialogFragment.Companion.PROMO_CODE_REQUEST_KEY
import com.pharmacy.myapp.checkout.model.TempDeliveryAddress
import com.pharmacy.myapp.checkout.model.TempPaymentMethod
import com.pharmacy.myapp.checkout.model.TempPharmacyAddress
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.toast
import com.pharmacy.myapp.data.DummyData
import com.pharmacy.myapp.ui.BuyerDeliveryAddress
import kotlinx.android.synthetic.main.fragment_checkout.*

class CheckoutFragment(private val viewModel: CheckoutViewModel) : BaseMVVMFragment(R.layout.fragment_checkout), View.OnClickListener {

    private val orderProductsAdapter = OrderProductsAdapter()
    private val radioButtonPadding by lazy { resources.getDimension(R.dimen._8sdp).toInt() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton(R.drawable.ic_arrow_back)

        viewBuyerDetailsCheckout.setData("Some full name", "+3801231231231", "test@exapmle.com")
        viewBuyerDeliveryAddressCheckout.setData(TempDeliveryAddress.newMockInstance(), TempPharmacyAddress.newMockInstance())
        viewBuyerDeliveryAddressCheckout.changeDeliveryMethod(BuyerDeliveryAddress.DeliveryMethod.DELIVERY)
        cardMethodDeliveryCheckout.isSelected = true
        cardMethodDeliveryCheckout.setOnClickListener(this)
        cardMethodPickupCheckout.setOnClickListener(this)

        initPaymentMethods()
        initOrderProducts()
        tvOrdersListEditCheckout.onClick { requireContext().toast("TODO edit order list") }
        btnPromoCodeCheckout.onClick {
            setFragmentResultListener(PROMO_CODE_REQUEST_KEY) { _, bundle ->
                val code = bundle[PromoCodeDialogFragment.PROMO_CODE_EXTRA_KEY]
                viewModel.handlePromoCodeResult(code as String)
            }
            doNav(actionCheckoutToPromoCodeDialog())
        }
        btnCheckoutOrderCheckout.onClick { requireContext().toast("TODO checkout") }

        tvTotalAmountCheckout.text = "123 ₽"
        tvDiscountCheckout.text = "321 ₽"
        tvDeliveryAmountCheckout.text = "\uD83D\uDE9B 382 ₽"
        tvTotalPayableCheckout.text = "999 ₽"
    }

    private fun initPaymentMethods() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        DummyData.paymentMethod.forEach {
            rgPaymentMethodsCheckout.addView(createPaymentRadioButton(layoutParams, it))
        }
    }

    private fun createPaymentRadioButton(
        layoutParams: ViewGroup.LayoutParams,
        it: TempPaymentMethod
    ): MaterialRadioButton {
        val radio = MaterialRadioButton(requireContext())
        radio.layoutParams = layoutParams
        radio.setPadding(radioButtonPadding, radioButtonPadding, radioButtonPadding, radioButtonPadding)
        radio.text = it.name
        radio.setCompoundDrawablesWithIntrinsicBounds(0, 0, it.icon, 0)
        return radio
    }

    private fun initOrderProducts() {
        val items = DummyData.getOrderProducts()
        rvOrdersListCheckout.setHasFixedSize(true)
        rvOrdersListCheckout.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvOrdersListCheckout.adapter = orderProductsAdapter
        orderProductsAdapter.setList(items)
    }

    override fun onClick(v: View?) {

        fun cardCheckout(deliveryMethod: BuyerDeliveryAddress.DeliveryMethod = BuyerDeliveryAddress.DeliveryMethod.PICKUP) {
            cardMethodPickupCheckout.isSelected = deliveryMethod == BuyerDeliveryAddress.DeliveryMethod.PICKUP
            cardMethodDeliveryCheckout.isSelected = deliveryMethod != BuyerDeliveryAddress.DeliveryMethod.PICKUP
            viewBuyerDeliveryAddressCheckout.changeDeliveryMethod(deliveryMethod)
        }

        when (v?.id) {
            cardMethodDeliveryCheckout.id -> cardCheckout(BuyerDeliveryAddress.DeliveryMethod.DELIVERY)
            cardMethodPickupCheckout.id -> cardCheckout(BuyerDeliveryAddress.DeliveryMethod.PICKUP)
        }
    }
}