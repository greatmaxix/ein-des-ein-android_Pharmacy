package com.pulse.components.checkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.checkout.CheckoutFragmentDirections.Companion.globalToPromoCodeDialog
import com.pulse.components.checkout.adapter.CheckoutProductsAdapter
import com.pulse.components.checkout.dialog.PromoCodeDialogFragment
import com.pulse.components.checkout.dialog.PromoCodeDialogFragment.Companion.PROMO_CODE_REQUEST_KEY
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.data.remote.model.order.DeliveryType
import com.pulse.databinding.FragmentCheckoutBinding
import com.pulse.ui.PharmacyAddressOrder

class CheckoutFragment(private val viewModel: CheckoutViewModel) : BaseMVVMFragment(R.layout.fragment_checkout), View.OnClickListener {

    private val args by navArgs<CheckoutFragmentArgs>()
    private val binding by viewBinding(FragmentCheckoutBinding::bind)
    private val orderProductsAdapter by lazy { CheckoutProductsAdapter(args.cartItem.products) }
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

        initOrderProducts()
        mbPromoCode.setDebounceOnClickListener {
            setFragmentResultListener(PROMO_CODE_REQUEST_KEY) { _, bundle ->
                val code = bundle[PromoCodeDialogFragment.PROMO_CODE_EXTRA_KEY]
                viewModel.handlePromoCodeResult(code as String)
            }
            doNav(globalToPromoCodeDialog())
        }
        mbCheckout.setDebounceOnClickListener { validateFieldsAndSendOrder() }

        val totalAmount = args.cartItem.totalPrice.toPriceFormat()
        mtvTotalAmount.text = totalAmount
        val deliveryCost = 150 // todo change in future
        mtvDeliveryAmount.text = getString(R.string.deliveryCost, deliveryCost)
        val totalCost = (args.cartItem.totalPrice + deliveryCost).toPriceFormat()
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