package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.res.use
import com.pulse.R
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.inflater
import com.pulse.core.extensions.visible
import com.pulse.data.remote.model.order.CustomerOrderData
import com.pulse.databinding.ViewBuyerDetailsOrderBinding

class BuyerDetailsOrder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewBuyerDetailsOrderBinding.inflate(inflater, this, true)
    private val padding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }
    private val bottomPadding by lazy { resources.getDimension(R.dimen._4sdp).toInt() }

    init {
        setPadding(padding, padding, padding, bottomPadding)
        attrs?.let { attrsSet ->
            context.theme.obtainStyledAttributes(attrsSet, R.styleable.BuyerDetailsView, defStyleAttr, -1)
                .use {
                    binding.mtvDelegateDetails.setText(it.getResourceId(R.styleable.BuyerDetailsOrderView_delegateLabelText, R.string.delegateDetails))
                }
        }
    }

    var customer: CustomerOrderData? = null
        set(value) {
            field = value
            field?.let(::updateContent)
        }

    private fun updateContent(customer: CustomerOrderData) = with(binding) {
        mtvBuyerFullName.text = customer.name
        val phoneHolder = "\uD83D\uDCF1${customer.phone}"
        mtvBuyerPhone.text = phoneHolder
        val emailHolder = "\uD83D\uDCEA ${customer.email}"
        mtvBuyerEmail.text = emailHolder
        if (customer.email.isNullOrEmpty()) mtvBuyerEmail.gone()
        if (!customer.delegateFullName.isNullOrBlank()) {
            mtvDelegateDetails.visible()
            mtvDelegateFullName.text = customer.delegateFullName
            mtvDelegateFullName.visible()
        } else {
            mtvDelegateDetails.gone()
            mtvDelegateFullName.gone()
        }
    }
}