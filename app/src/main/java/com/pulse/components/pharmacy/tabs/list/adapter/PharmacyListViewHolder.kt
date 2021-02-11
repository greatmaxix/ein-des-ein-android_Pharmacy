package com.pulse.components.pharmacy.tabs.list.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.pharmacy.model.Pharmacy
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.getString
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.loadGlideDrawableByURL
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.ItemPharmacyBinding

class PharmacyListViewHolder(
    override val containerView: View,
    private val addToCart: (Pharmacy) -> Unit,
    private val makeCall: (String) -> Unit,
    private val geoNav: (Pharmacy) -> Unit
) : BaseViewHolder<Pharmacy>(containerView) {

    private val binding by viewBinding(ItemPharmacyBinding::bind)

    override fun bind(item: Pharmacy) = with(binding) {
        ivPharmacy.loadGlideDrawableByURL(item.logo.url)
        mtvName.text = item.name
        mtvAddress.text = item.location.address
        mtvPhone.text = getString(R.string.pharmacyPhoneWith, item.phone)
        mtvPhone.setDebounceOnClickListener { makeCall(item.phone) }
        mtvProductPrice.text = getString(R.string.price, item.firstProductPrice)
        fabLocation.setDebounceOnClickListener { geoNav(item) }
        mbAddToCart.setDebounceOnClickListener { addToCart(item) }
    }

    companion object {

        fun newInstance(parent: ViewGroup, addToCart: (Pharmacy) -> Unit, makeCall: (String) -> Unit, geoNav: (Pharmacy) -> Unit) =
            PharmacyListViewHolder(parent.inflate(R.layout.item_pharmacy), addToCart, makeCall, geoNav)
    }
}
