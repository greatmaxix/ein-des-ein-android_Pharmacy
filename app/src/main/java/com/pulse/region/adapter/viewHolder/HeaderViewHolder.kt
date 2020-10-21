package com.pulse.region.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.model.region.RegionWithHeader
import kotlinx.android.synthetic.main.item_region_header.view.*

class HeaderViewHolder(view: View) : BaseViewHolder<RegionWithHeader>(view) {
    override fun bind(item: RegionWithHeader) {
        itemView.tvmHeaderLetter.text = item.header.toString()
    }

    companion object {

        fun newInstance(parent: ViewGroup) = HeaderViewHolder(parent.inflate(R.layout.item_region_header))
    }
}
