package com.pulse.orders.details

import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.navArgs
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.pulse.R
import com.pulse.checkout.adapter.CheckoutProductsAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.data.remote.DummyData.paymentMethod
import com.pulse.model.order.Order
import com.pulse.model.order.OrderStatus
import com.pulse.ui.OrderSteps
import com.pulse.ui.PharmacyAddressOrder
import kotlinx.android.synthetic.main.fragment_order_details.*

class OrderDetailsFragment(private val viewModel: OrderDetailsViewModel) : BaseMVVMFragment(R.layout.fragment_order_details) {

    private val args: OrderDetailsFragmentArgs by navArgs()

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

    private fun updateOrderInfo(order: Order) {
        val isCancelled = order.orderStatus.isCanceled

        val subList = OrderStatus.getStatusesList()
            .map { OrderSteps.Step(it.getStatusRes, order.orderStatus == it) } // todo refactor on demand
        orderDetailsSteps.setSteps(subList)

        orderDetailsSteps.visibleOrGone(!isCancelled)
        if (isCancelled) {
            statusTitleOrderDetails.text = getString(R.string.orderWithIdCanceled, order.id)
            statusDescriptionOrderDetails.text = getString(R.string.orderCanceled)
            toolbarContainerOrderDetails.background.setColorFilter(R.color.grey.toColor, PorterDuff.Mode.SRC_ATOP)
        }
        statusTitleOrderDetails.visibleOrGone(isCancelled)
        statusDescriptionOrderDetails.visibleOrGone(isCancelled)

        viewBuyerInfoOrderDetails.customer = order.contactInfo
        val comment = order.deliveryInfo.comment
        if (comment.isNullOrEmpty()) {
            mcvNoteOrderDetails.gone()
        }

        with(args.order.pharmacy) {
            pharmacyAddressOrder.makeDial { showDial(it) }
            pharmacyAddressOrder.pharmacy = PharmacyAddressOrder.PharmacyInfo(logo.url, name, location.address, phone)
        }

        tvNoteOrderDetails.text = comment

        if (order.deliveryInfo.deliveryType?.isDelivery.falseIfNull()) {
            viewBuyerDeliveryAddressOrderDetails.visible()
            viewBuyerDeliveryAddressOrderDetails.deliveryAddress = order.deliveryInfo.addressOrderData
        }

        tvTotalPayableOrderDetails.text = getString(R.string.orderCost, order.pharmacyProductsTotalPrice.formatPrice())

        paymentMethod[2].let { // todo refactor on demand
            tvPaymentTypeOrderDetails.text = it.name
            tvPaymentTypeOrderDetails.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, it.icon, 0)
        }

        initOrderProducts()

        btnCancelOrder.visibleOrGone(order.orderStatus.isNew)
        btnCancelOrder.setDebounceOnClickListener { viewModel.cancelOrder(order.id) }
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

    private fun initOrderProducts() {
        rvProductsOrderDetails.setHasFixedSize(true)
        rvProductsOrderDetails.adapter = CheckoutProductsAdapter(args.order.pharmacyProductOrderDataList.toMutableList())
    }
}