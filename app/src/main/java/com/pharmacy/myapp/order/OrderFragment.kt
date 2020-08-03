package com.pharmacy.myapp.order

import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.adapter.OrderProductsAdapter
import com.pharmacy.myapp.checkout.model.TempDeliveryAddress
import com.pharmacy.myapp.checkout.model.TempPharmacyAddress
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.toast
import com.pharmacy.myapp.core.extensions.visibleOrGone
import com.pharmacy.myapp.data.DummyData
import com.pharmacy.myapp.data.DummyData.paymentMethod
import com.pharmacy.myapp.ui.BuyerDeliveryAddress
import com.pharmacy.myapp.ui.OrderSteps
import kotlinx.android.synthetic.main.fragment_order.*
import timber.log.Timber
import kotlin.random.Random

class OrderFragment(private val viewModel: OrderViewModel) : BaseMVVMFragment(R.layout.fragment_order) {

    private val orderProductsAdapter = OrderProductsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initOrderProducts()

        val isCancelled = Random.nextBoolean()
        orderSteps.visibleOrGone(!isCancelled)
        if (isCancelled) {
            statusTitleOrder.text = "Ваш заказ № 12583 отменен"
            statusDescriptionOrder.text = "Отменен аптекой"
            toolbarContainerOrder.background.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey), PorterDuff.Mode.SRC_ATOP)
        }
        statusTitleOrder.visibleOrGone(isCancelled)
        statusDescriptionOrder.visibleOrGone(isCancelled)

        Timber.e("$isCancelled")
        viewBuyerDetailsOrder.setData("Some full name", "+3801231231231", "test@exapmle.com")
        viewBuyerDeliveryAddressOrder.setData(TempDeliveryAddress.newMockInstance(), TempPharmacyAddress.newMockInstance())
        viewBuyerDeliveryAddressOrder.changeDeliveryMethod(BuyerDeliveryAddress.DeliveryMethod.values().random())
        tvNoteOrder.text = "Оставьте у двери, предварительно позвонив 4 раза в дверь"

        tvPaymentTypeEditOrder.onClick { requireContext().toast("TODO edit payment method") }
        tvOrdersListEditOrder.onClick { requireContext().toast("TODO edit order list") }
        btnCheckoutOrder.onClick { requireContext().toast("TODO order") }

        tvTotalAmountOrder.text = "123 ₽"
        tvDiscountOrder.text = "321 ₽"
        tvDeliveryAmountOrder.text = "\uD83D\uDE9B 382 ₽"
        tvTotalPayableOrder.text = "999 ₽"

        paymentMethod.random().let {
            tvPaymentTypeOrder.text = it.name
            tvPaymentTypeOrder.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, it.icon, 0)
        }
        orderSteps.setSteps(
            arrayListOf(
                OrderSteps.Step(R.string.promoCodeHint, false),
                OrderSteps.Step(R.string.promoCodeApply, true),
                OrderSteps.Step(R.string.promoCodeCancel, false),
                OrderSteps.Step(R.string.promoCodeHint, false)
            )
        )
    }

    private fun initToolbar() {
        showBackButton(R.drawable.ic_arrow_back) { navController.popBackStack() }
        toolbar?.let {
            it.title = "№ 12345"

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
                    setShadowColor(ContextCompat.getColor(requireContext(), R.color.primaryBlueRipple))
                    setTint(ContextCompat.getColor(requireContext(), R.color.primaryBlue))
                    paintStyle = Paint.Style.FILL
                }
            ViewCompat.setBackground(it, shape)
        }
    }

    private fun initOrderProducts() {
        val items = DummyData.getOrderProducts()
        rvOrdersListOrder.setHasFixedSize(true)
        rvOrdersListOrder.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvOrdersListOrder.adapter = orderProductsAdapter
        orderProductsAdapter.setList(items)
    }
}