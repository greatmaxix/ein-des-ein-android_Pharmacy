package com.pharmacy.pharmacy.tabs.list.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.R
import com.pharmacy.core.base.adapter.BaseViewHolder
import com.pharmacy.core.extensions.inflate
import com.pharmacy.core.extensions.loadGlide
import com.pharmacy.core.extensions.onClick
import com.pharmacy.core.extensions.stringRes
import com.pharmacy.pharmacy.model.Pharmacy
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
        ivPharmacy.loadGlide(item.logo.url)
        tvPharmacyName.text = item.name
        tvPharmacyAddress.text = item.location.address
        tvPharmacyPhone.text = stringRes(R.string.pharmacyPhoneWith, item.phone)
        tvPharmacyPhone.onClick { makeCall(item.phone) }
        tvProductPrice.text = stringRes(R.string.price, item.firstProductPrice)
        ivPharmacyLocation.onClick { geoNav(item) }
        mbProductAddToCart.onClick { addToCart(item) }
    }

    companion object {
        @JvmStatic
        fun newInstance(parent: ViewGroup, addToCart: (Pharmacy) -> Unit, makeCall: (String) -> Unit, geoNav: (Pharmacy) -> Unit) =
            PharmacyListViewHolder(parent.inflate(R.layout.item_pharmacy), addToCart, makeCall, geoNav)
    }
}
