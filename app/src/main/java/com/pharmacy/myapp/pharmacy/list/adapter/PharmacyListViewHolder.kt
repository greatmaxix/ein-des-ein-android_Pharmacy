package com.pharmacy.myapp.pharmacy.list.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.pharmacy.model.Pharmacy
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.loadGlide
import kotlinx.android.synthetic.main.item_checkout_store.view.*

class PharmacyListViewHolder(override val containerView: View, private val click: (Pharmacy) -> Unit) : BaseViewHolder<Pharmacy>(containerView) {

    //TODO check why @synthetic@ dont work
    override fun bind(item: Pharmacy) = with(itemView) {
        ivPharmacy.loadGlide(item.logo.url)
        tvPharmacyName.text = item.name
        tvPharmacyAddress.text = item.location.address
    }

    companion object {
        @JvmStatic
        fun newInstance(parent: ViewGroup, click: (Pharmacy) -> Unit) = PharmacyListViewHolder(parent.inflate(R.layout.item_checkout_store), click)
    }
}
