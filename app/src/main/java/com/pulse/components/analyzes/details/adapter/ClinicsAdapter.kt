package com.pulse.components.analyzes.details.adapter

import android.view.ViewGroup
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.core.base.adapter.BaseRecyclerAdapter
import com.pulse.core.extensions.onClickDebounce

class ClinicsAdapter(
    private val itemClick: (Clinic) -> Unit,
    private val orderService: ((Clinic) -> Unit)?,
    private val makeCall: (String) -> Unit,
    private val geoNav: (Clinic) -> Unit
) : BaseRecyclerAdapter<Clinic, ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ClinicViewHolder.newInstance(parent, orderService, makeCall, geoNav)
        .apply { itemView.onClickDebounce { itemClick(getItem(bindingAdapterPosition)) } }

    fun notifyItems(newItems: List<Clinic>) {
        items = newItems.toMutableList()
    }
}