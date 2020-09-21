package com.pharmacy.myapp.pharmacy.tabs.list.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.pharmacy.model.Pharmacy
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.loadGlide
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.stringRes
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
