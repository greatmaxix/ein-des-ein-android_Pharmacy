package com.pulse.checkout

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import com.google.android.material.radiobutton.MaterialRadioButton
import com.pulse.R
import com.pulse.checkout.CheckoutFragmentDirections.Companion.actionCheckoutToPromoCodeDialog
import com.pulse.checkout.adapter.CheckoutProductsAdapter
import com.pulse.checkout.dialog.PromoCodeDialogFragment
import com.pulse.checkout.dialog.PromoCodeDialogFragment.Companion.PROMO_CODE_REQUEST_KEY
import com.pulse.checkout.model.TempPaymentMethod
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.data.remote.DummyData
import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.data.remote.model.order.DeliveryType
import com.pulse.ui.PharmacyAddressOrder
import kotlinx.android.synthetic.main.fragment_checkout.*

class CheckoutFragment(private val viewModel: CheckoutViewModel) : BaseMVVMFragment(R.layout.fragment_checkout), View.OnClickListener {

    private val args by navArgs<CheckoutFragmentArgs>()

    private val orderProductsAdapter by lazy { CheckoutProductsAdapter(args.cartItem.products) }
    private val radioButtonPadding by lazy { resources.getDimension(R.dimen._8sdp).toInt() }

    private val Boolean.deliveryType
        get() = if (this) DeliveryType.DELIVERY else DeliveryType.PICKUP

    private val Boolean.deliveryAddress
        get() = if (this) viewBuyerDeliveryAddressCheckout.obtainAddress() else null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        cardMethodDeliveryCheckout.isSelected = true
        cardMethodDeliveryCheckout.setOnClickListener(this)
        cardMethodPickupCheckout.setOnClickListener(this)

        initPaymentMethods()
        initOrderProducts()
        btnPromoCodeCheckout.onClick {
            setFragmentResultListener(PROMO_CODE_REQUEST_KEY) { _, bundle ->
                val code = bundle[PromoCodeDialogFragment.PROMO_CODE_EXTRA_KEY]
                viewModel.handlePromoCodeResult(code as String)
            }
            doNav(actionCheckoutToPromoCodeDialog())
        }
        btnCheckoutOrderCheckout.onClick { validateFieldsAndSendOrder() }

        val totalAmount = "${args.cartItem.totalPrice.formatPrice()} ₸"
        tvTotalAmountCheckout.text = totalAmount
        val deliveryCost = 150 // todo change in future
        tvDeliveryAmountCheckout.text = getString(R.string.deliveryCost, deliveryCost)
        val totalCost = "${(args.cartItem.totalPrice + deliveryCost).formatPrice()} ₸"
        tvTotalPayableCheckout.text = totalCost

        setPharmacyInfo()
    }

    private fun setPharmacyInfo() = with(args.cartItem) {
        pharmacyAddressCheckout.pharmacy = PharmacyAddressOrder.PharmacyInfo(logo.url, name, location.address)
    }

    private fun validateFieldsAndSendOrder() {
        val isDelivery = cardMethodDeliveryCheckout.isSelected
        if (viewBuyerDetailsCheckout.isFieldsValid() && (!isDelivery || viewBuyerDeliveryAddressCheckout.validateFields())) {
            val deliveryInfo = DeliveryInfoOrderData(tilOrderNote.text(), isDelivery.deliveryAddress, isDelivery.deliveryType)
            viewModel.sendOrder(viewBuyerDetailsCheckout.getDetail(), deliveryInfo, args.cartItem)
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.directionLiveData, navController::navigate)

        observe(viewModel.customerInfoLiveData) {
            viewBuyerDetailsCheckout.setData(it?.name, it?.phone?.addPlusSignIfNeeded(), it?.email)
        }
        observe(viewModel.addressLiveData) {
            it.addressOrderData?.let(viewBuyerDeliveryAddressCheckout::setAddress)
            etOrderNote.setText(it.comment)
        }
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
    ) = MaterialRadioButton(requireContext()).apply {
        this.layoutParams = layoutParams
        setPadding(radioButtonPadding, radioButtonPadding, radioButtonPadding, radioButtonPadding)
        text = it.name
        setCompoundDrawablesWithIntrinsicBounds(0, 0, it.icon, 0)
        isEnabled = it.isChecked
        isChecked = it.isChecked
    }

    private fun initOrderProducts() {
        rvProductsListCheckout.setHasFixedSize(true)
        rvProductsListCheckout.adapter = orderProductsAdapter
    }

    override fun onClick(v: View?) {

        fun setDeliveryMethod(isPickup: Boolean) {
            cardMethodDeliveryCheckout.isSelected = !isPickup
            cardMethodPickupCheckout.isSelected = isPickup
            viewBuyerDeliveryAddressCheckout.visibleOrGone(!isPickup)
        }

        when (v?.id) {
            cardMethodDeliveryCheckout.id -> setDeliveryMethod(false)
            cardMethodPickupCheckout.id -> setDeliveryMethod(true)
        }
    }
}