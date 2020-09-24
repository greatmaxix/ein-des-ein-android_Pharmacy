package com.pharmacy.myapp.checkout

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.radiobutton.MaterialRadioButton
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.CheckoutFragmentDirections.Companion.actionCheckoutToPromoCodeDialog
import com.pharmacy.myapp.checkout.CheckoutFragmentDirections.Companion.globalToOrder
import com.pharmacy.myapp.checkout.adapter.CheckoutProductsAdapter
import com.pharmacy.myapp.checkout.dialog.PromoCodeDialogFragment
import com.pharmacy.myapp.checkout.dialog.PromoCodeDialogFragment.Companion.PROMO_CODE_REQUEST_KEY
import com.pharmacy.myapp.checkout.model.TempDeliveryAddress
import com.pharmacy.myapp.checkout.model.TempPaymentMethod
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addPlusSignIfNeeded
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.visibleOrGone
import com.pharmacy.myapp.data.DummyData
import kotlinx.android.synthetic.main.fragment_checkout.*

class CheckoutFragment(private val viewModel: CheckoutViewModel) : BaseMVVMFragment(R.layout.fragment_checkout), View.OnClickListener {

    private val args by navArgs<CheckoutFragmentArgs>()

    private val orderProductsAdapter by lazy { CheckoutProductsAdapter(args.cartItem.products) }
    private val radioButtonPadding by lazy { resources.getDimension(R.dimen._8sdp).toInt() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        viewBuyerDeliveryAddressCheckout.setData(TempDeliveryAddress.newInstance())
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
        btnCheckoutOrderCheckout.onClick { validateFields() }

        tvTotalAmountCheckout.text = "${args.cartItem.totalPrice.toPlainString()} ₸"
//        tvDiscountCheckout.text = "321 ₸"
        val deliveryCost = 150
        tvDeliveryAmountCheckout.text = "\uD83D\uDE9B $deliveryCost ₸"
        tvTotalPayableCheckout.text = "${args.cartItem.totalPrice.plus(deliveryCost.toBigDecimal()).toPlainString()} ₸"

        setPharmacyInfo()
    }

    private fun setPharmacyInfo() = with(args.cartItem) {
        Glide.with(ivPharmacyLogoCheckout)
            .load(logo.url)
            .error(R.drawable.ic_drugstore_base)
            .into(ivPharmacyLogoCheckout)
        tvPharmacyNameCheckout.text = name
        val cityAndStreetHolder = "\uD83C\uDFE0 ${location.address}"
        tvPharmacyAddressOrder.text = cityAndStreetHolder
    }

    private fun validateFields() {
        if (viewBuyerDetailsCheckout.isFieldsValid() && viewBuyerDeliveryAddressCheckout.validateFields()) {
            val detail = viewBuyerDetailsCheckout.getDetail() // todo
            if (cardMethodDeliveryCheckout.isSelected) {
                val deliveryAddress = viewBuyerDeliveryAddressCheckout.obtainDeliveryAddress() // todo
            }
//            viewModel.sendOrder(detail, deliveryAddress, paymentType, checkoutNote) // todo
            navController.navigate(globalToOrder())
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.customerInfoLiveData) {
            viewBuyerDetailsCheckout.setData(it.name, it.phone.addPlusSignIfNeeded(), it.email)
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
    ): MaterialRadioButton {
        val radio = MaterialRadioButton(requireContext())
        radio.layoutParams = layoutParams
        radio.setPadding(radioButtonPadding, radioButtonPadding, radioButtonPadding, radioButtonPadding)
        radio.text = it.name
        radio.setCompoundDrawablesWithIntrinsicBounds(0, 0, it.icon, 0)
        radio.isEnabled = it.isChecked
        radio.isChecked = it.isChecked
        return radio
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