package com.pulse.components.checkout

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.radiobutton.MaterialRadioButton
import com.pulse.R
import com.pulse.components.checkout.CheckoutFragmentDirections.Companion.actionCheckoutToPromoCodeDialog
import com.pulse.components.checkout.adapter.CheckoutProductsAdapter
import com.pulse.components.checkout.dialog.PromoCodeDialogFragment
import com.pulse.components.checkout.dialog.PromoCodeDialogFragment.Companion.PROMO_CODE_REQUEST_KEY
import com.pulse.components.checkout.model.TempPaymentMethod
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.data.remote.DummyData
import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.data.remote.model.order.DeliveryType
import com.pulse.databinding.FragmentCheckoutBinding
import com.pulse.ui.PharmacyAddressOrder
import com.pulse.util.ColorFilterUtil

class CheckoutFragment(private val viewModel: CheckoutViewModel) : BaseMVVMFragment(R.layout.fragment_checkout), View.OnClickListener {

    private val args by navArgs<CheckoutFragmentArgs>()
    private val binding by viewBinding(FragmentCheckoutBinding::bind)
    private val orderProductsAdapter by lazy { CheckoutProductsAdapter(args.cartItem.products) }
    private val radioButtonPadding by lazy { resources.getDimension(R.dimen._8sdp).toInt() }
    private val fontSemibold by lazyGetFont(R.font.open_sans_semi_bold)
    private val fontNormal by lazyGetFont(R.font.open_sans_regular)
    private val Boolean.deliveryType
        get() = if (this) DeliveryType.DELIVERY else DeliveryType.PICKUP

    private val Boolean.deliveryAddress
        get() = if (this) binding.viewBuyerDeliveryAddress.obtainAddress() else null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        mcvMethodDelivery.isSelected = true
        mtvMethodDelivery.typeface = fontSemibold
        mcvMethodDelivery.setOnClickListener(this@CheckoutFragment)
        mcvMethodPickup.setOnClickListener(this@CheckoutFragment)

        initPaymentMethods()
        initOrderProducts()
        mbPromoCode.setDebounceOnClickListener {
            setFragmentResultListener(PROMO_CODE_REQUEST_KEY) { _, bundle ->
                val code = bundle[PromoCodeDialogFragment.PROMO_CODE_EXTRA_KEY]
                viewModel.handlePromoCodeResult(code as String)
            }
            doNav(actionCheckoutToPromoCodeDialog())
        }
        mbCheckout.setDebounceOnClickListener { validateFieldsAndSendOrder() }

        val totalAmount = "${args.cartItem.totalPrice.formatPrice()} ₸"
        mtvTotalAmount.text = totalAmount
        val deliveryCost = 150 // todo change in future
        mtvDeliveryAmount.text = getString(R.string.deliveryCost, deliveryCost)
        val totalCost = "${(args.cartItem.totalPrice + deliveryCost).formatPrice()} ₸"
        mtvTotalPayable.text = totalCost

        setPharmacyInfo()
    }

    private fun setPharmacyInfo() = with(args.cartItem) {
        binding.viewPharmacyAddress.pharmacy = PharmacyAddressOrder.PharmacyInfo(logo.url, name, location.address, phone)
    }

    private fun validateFieldsAndSendOrder() {
        val isDelivery = binding.mcvMethodDelivery.isSelected
        if (binding.viewBuyerDetails.isFieldsValid() && (!isDelivery || binding.viewBuyerDeliveryAddress.validateFields())) {
            val deliveryInfo = DeliveryInfoOrderData(binding.tilOrderNote.text(), isDelivery.deliveryAddress, isDelivery.deliveryType)
            viewModel.sendOrder(binding.viewBuyerDetails.getDetail(), deliveryInfo, args.cartItem)
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.customerInfoLiveData) {
            binding.viewBuyerDetails.setData(it?.name, it?.phone?.addPlusSignIfNeeded(), it?.email)
        }
        observe(viewModel.addressLiveData) {
            it.addressOrderData?.let(binding.viewBuyerDeliveryAddress::setAddress)
            binding.etOrderNote.setText(it.comment)
        }
    }

    private fun initPaymentMethods() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        DummyData.paymentMethod.forEach {
            binding.rgPaymentMethods.addView(createPaymentRadioButton(layoutParams, it))
        }
    }

    private fun createPaymentRadioButton(layoutParams: ViewGroup.LayoutParams, it: TempPaymentMethod) = MaterialRadioButton(requireContext()).apply {
        this.layoutParams = layoutParams
        setPadding(radioButtonPadding, (radioButtonPadding * 1.5).toInt(), radioButtonPadding, (radioButtonPadding * 1.5).toInt())
        text = it.name
        val drawable = getDrawable(it.icon)?.apply { if (!it.isChecked) colorFilter = ColorFilterUtil.blackWhiteFilter }
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        setTextColor(getColorStateList(R.color.selector_text_payment))
        buttonTintList = getColorStateList(R.color.selector_tint_button_payment)
        isEnabled = it.isChecked
        isChecked = it.isChecked
    }

    private fun initOrderProducts() = with(binding.rvProducts) {
        setHasFixedSize(true)
        adapter = orderProductsAdapter
    }

    override fun onClick(v: View?) = with(binding) {
        fun setDeliveryMethod(isPickup: Boolean) {
            mcvMethodDelivery.isSelected = !isPickup
            mtvMethodDelivery.typeface = if (!isPickup) fontSemibold else fontNormal
            mcvMethodPickup.isSelected = isPickup
            mtvMethodPickup.typeface = if (isPickup) fontSemibold else fontNormal
            viewBuyerDeliveryAddress.visibleOrGone(!isPickup)
            mtvBuyerDeliveryAddressTitle.visibleOrGone(!isPickup)
        }

        when (v?.id) {
            mcvMethodDelivery.id -> setDeliveryMethod(false)
            mcvMethodPickup.id -> setDeliveryMethod(true)
        }
    }
}