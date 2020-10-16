package com.pharmacy.components.pharmacy.tabs.list.adapter

import android.view.ViewGroup
import com.pharmacy.components.pharmacy.model.Pharmacy
import com.pharmacy.core.base.adapter.BaseRecyclerAdapter

class PharmacyListAdapter(private val addToCart: (Pharmacy) -> Unit, private val makeCall: (String) -> Unit, private val geoNav: (Pharmacy) -> Unit) : BaseRecyclerAdapter<Pharmacy, PharmacyListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PharmacyListViewHolder.newInstance(parent, addToCart, makeCall, geoNav)

    fun notifyItems(newItems: List<Pharmacy>) {
        items = newItems.toMutableList()
    }
}