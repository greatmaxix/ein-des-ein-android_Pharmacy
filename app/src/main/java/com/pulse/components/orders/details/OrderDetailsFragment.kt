package com.pulse.components.orders.details

import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.pulse.R
import com.pulse.components.checkout.adapter.CheckoutProductsAdapter
import com.pulse.components.checkout.model.PaymentMethodModel
import com.pulse.components.payments.model.PaymentMethod
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentOrderDetailsBinding
import com.pulse.model.order.Order
import com.pulse.model.order.OrderStatus
import com.pulse.ui.OrderSteps
import com.pulse.ui.PharmacyAddressOrder

class OrderDetailsFragment(private val viewModel: OrderDetailsViewModel) : BaseMVVMFragment(R.layout.fragment_order_details) {

    private val args: OrderDetailsFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentOrderDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        updateOrderInfo(args.order)
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.isCancelSuccessLiveData) {
            if (it) updateOrderInfo(args.order.copy(orderStatus = OrderStatus.CANCELED))
        }
    }

    private fun updateOrderInfo(order: Order) = with(binding) {
        val isCancelled = order.orderStatus.isCanceled

        val subList = OrderStatus.getStatusesList()
            .map { OrderSteps.Step(it.getStatusRes, order.orderStatus == it) } // todo refactor on demand
        viewOrderSteps.setSteps(subList)

        viewOrderSteps.visibleOrGone(!isCancelled)
        if (isCancelled) {
            nsvContainer.smoothScrollTo(0, 0, 500)
            mtvStatusTitle.text = getString(R.string.orderWithIdCanceled, order.id)
            mtvStatusDescription.text = getString(R.string.orderCanceled)
            llToolbarContainer.background.setColorFilter(R.color.grey.toColor, PorterDuff.Mode.SRC_ATOP)
        }
        mtvStatusTitle.visibleOrGone(isCancelled)
        mtvStatusDescription.visibleOrGone(isCancelled)

        viewBuyerInfo.customer = order.contactInfo
        val comment = order.deliveryInfo.comment
        if (comment.isNullOrEmpty()) {
            mcvNote.gone()
        }

        with(args.order.pharmacy) {
            viewPharmacyAddress.makeDial { showDial(it) }
            viewPharmacyAddress.pharmacy = PharmacyAddressOrder.PharmacyInfo(logo.url, name, location.address, phone)
        }

        mtvNote.text = comment

        if (order.deliveryInfo.deliveryType?.isDelivery.falseIfNull()) {
            viewBuyerDeliveryAddress.visible()
            viewBuyerDeliveryAddress.deliveryAddress = order.deliveryInfo.addressOrderData
        }

        mtvTotalPayable.text = getString(R.string.orderCost, order.pharmacyProductsTotalPrice.formatPrice())

        PaymentMethodModel(PaymentMethod.values()[3], true).let { // TODO change to proper value
            mtvPaymentType.text = getString(it.method.title)
            mtvPaymentType.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, it.method.icon, 0)
        }
        initOrderProducts()

        mbCancel.visibleOrGone(order.orderStatus.isNew)
        mbCancel.setDebounceOnClickListener { viewModel.cancelOrder(order.id) }
    }

    private fun initToolbar() {
        showBackButton()
        toolbar?.let {
            it.title = getString(R.string.orderNumber, args.order.id)

            val radius = resources.getDimension(R.dimen._8sdp)
            val appearanceModel = ShapeAppearanceModel()
                .toBuilder()
                .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
                .setBottomRightCorner(CornerFamily.ROUNDED, radius)
                .build()

            val shape = MaterialShapeDrawable(appearanceModel)
                .apply {
                    shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
                    elevation = 0.1f
                    setShadowColor(R.color.colorRippleBlue.toColor)
                    setTint(R.color.primaryBlue.toColor)
                    paintStyle = Paint.Style.FILL
                }
            ViewCompat.setBackground(it, shape)
        }
    }

    private fun initOrderProducts() = with(binding.rvProducts) {
        setHasFixedSize(true)
        adapter = CheckoutProductsAdapter(args.order.pharmacyProductOrderDataList.toMutableList())
    }
}