package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.pulse.R
import com.pulse.core.extensions.*
import com.pulse.databinding.ViewPharmacyAddressOrderBinding

class PharmacyAddressOrder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding = ViewPharmacyAddressOrderBinding.inflate(inflater, this, true)
    private val contentPadding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }

    var pharmacy: PharmacyInfo? = null
        set(value) {
            field = value
            field?.let(::updatePharmacy)
        }

    init {
        setCardBackgroundColor(getColor(R.color.colorGlobalWhite))
        setContentPadding(contentPadding, contentPadding, contentPadding, contentPadding)
        radius = resources.getDimension(R.dimen._10sdp)
        clipToPadding = false
        clipChildren = false
        cardElevation = 0F
    }

    private fun updatePharmacy(info: PharmacyInfo) = with(info) {
        binding.ivPharmacyLogo.loadGlideDrugstore(logo)
        binding.mtvPharmacyName.text = name
        binding.mtvPharmacyAddress.text = context.getString(R.string.cityStreetHolder, info.address)
        binding.mtvPharmacyPhone.text = context.getString(R.string.phoneHolder, info.phone)
    }

    fun makeDial(makeDial: (String) -> Unit) = binding.mtvPharmacyPhone.setDebounceOnClickListener { makeDial(binding.mtvPharmacyPhone.text().substring(3)) }

    data class PharmacyInfo(val logo: String, val name: String, val address: String, val phone: String)
}