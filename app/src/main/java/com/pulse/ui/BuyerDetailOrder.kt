package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.pulse.R
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.inflate
import com.pulse.data.remote.model.order.CustomerOrderData
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_buyer_details_order.view.*

class BuyerDetailsOrder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_buyer_details_order, true)

    private val padding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }
    private val bottomPadding by lazy { resources.getDimension(R.dimen._4sdp).toInt() }

    init {
        setPadding(padding, padding, padding, bottomPadding)
    }

    var customer: CustomerOrderData? = null
        set(value) {
            field = value
            field?.let(::updateContent)
        }

    private fun updateContent(customer: CustomerOrderData) {
        tvBuyerFullNameOrder.text = customer.name
        val phoneHolder = "\uD83D\uDCF1${customer.phone}"
        tvBuyerPhoneOrder.text = phoneHolder
        val emailHolder = "\uD83D\uDCEA ${customer.email}"
        tvBuyerEmailOrder.text = emailHolder
        if (customer.email.isNullOrEmpty()) {
            tvBuyerEmailOrder.gone()
        }
    }
}