package com.pharmacy.myapp.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.loadGlideDrugstore
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_pharmacy_address_order.view.*

class PharmacyAddressOrder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_pharmacy_address_order, true)

    private val contentPadding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }

    private var pharmacy: Triple<String, String, String>? = null

    init {
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorGlobalWhite))
        setContentPadding(contentPadding, contentPadding, contentPadding, contentPadding)
        radius = resources.getDimension(R.dimen._10sdp)
        clipToPadding = false
        clipChildren = false
        cardElevation = 0F
    }

    fun setData(address: Triple<String, String, String>? = null) {
        if (address != null) {
            this.pharmacy = address
        }
        updatePharmacy()
    }

    private fun updatePharmacy() {
        with(pharmacy ?: return) {
            ivPharmacyLogoCheckout.loadGlideDrugstore(first)
            tvPharmacyNameCheckout.text = second
            tvPharmacyAddressOrder.text = context.getString(R.string.cityStreetHolder, third)
        }
    }

}