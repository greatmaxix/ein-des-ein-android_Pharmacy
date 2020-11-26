package com.pulse.components.pharmacy.tabs.list.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.components.pharmacy.model.Pharmacy
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.loadGlideDrawableByURL
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.stringRes
import kotlinx.android.synthetic.main.item_pharmacy.view.*

class PharmacyListViewHolder(
    override val containerView: View,
    private val addToCart: (Pharmacy) -> Unit,
    private val makeCall: (String) -> Unit,
    private val geoNav: (Pharmacy) -> Unit
) :
    BaseViewHolder<Pharmacy>(containerView) {

    //TODO check why @synthetic@ dont work
    override fun bind(item: Pharmacy) = with(itemView) {
        ivPharmacy.loadGlideDrawableByURL(item.logo.url)
        tvPharmacyName.text = item.name
        tvPharmacyAddress.text = item.location.address
        tvPharmacyPhone.text = stringRes(R.string.pharmacyPhoneWith, item.phone)
        tvPharmacyPhone.setDebounceOnClickListener { makeCall(item.phone) }
        tvProductPrice.text = stringRes(R.string.price, item.firstProductPrice)
        ivPharmacyLocation.setDebounceOnClickListener { geoNav(item) }
        mbProductAddToCart.setDebounceOnClickListener { addToCart(item) }
    }

    companion object {
        @JvmStatic
        fun newInstance(parent: ViewGroup, addToCart: (Pharmacy) -> Unit, makeCall: (String) -> Unit, geoNav: (Pharmacy) -> Unit) =
            PharmacyListViewHolder(parent.inflate(R.layout.item_pharmacy), addToCart, makeCall, geoNav)
    }
}
