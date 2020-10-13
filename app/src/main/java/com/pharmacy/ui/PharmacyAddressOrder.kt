package com.pharmacy.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.card.MaterialCardView
import com.pharmacy.R
import com.pharmacy.core.extensions.colorCompat
import com.pharmacy.core.extensions.inflate
import com.pharmacy.core.extensions.loadGlideDrugstore
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_pharmacy_address_order.view.*

class PharmacyAddressOrder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_pharmacy_address_order, true)

    private val contentPadding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }

    var pharmacy: PharmacyInfo? = null
        set(value) {
            field = value
            field?.let(::updatePharmacy)
        }

    init {
        setCardBackgroundColor(colorCompat(R.color.colorGlobalWhite))
        setContentPadding(contentPadding, contentPadding, contentPadding, contentPadding)
        radius = resources.getDimension(R.dimen._10sdp)
        clipToPadding = false
        clipChildren = false
        cardElevation = 0F
    }

    private fun updatePharmacy(info: PharmacyInfo) = with(info) {
        ivPharmacyLogoCheckout.loadGlideDrugstore(logo)
        tvPharmacyNameCheckout.text = name
        tvPharmacyAddressOrder.text = context.getString(R.string.cityStreetHolder, info.address)
    }

    data class PharmacyInfo(val logo: String, val name: String, val address: String)

}