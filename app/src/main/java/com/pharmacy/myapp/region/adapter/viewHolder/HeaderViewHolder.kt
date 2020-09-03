package com.pharmacy.myapp.region.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.model.region.RegionWithHeader
import kotlinx.android.synthetic.main.item_region_header.view.*

class HeaderViewHolder(view: View) : BaseViewHolder<RegionWithHeader>(view) {
    override fun bind(item: RegionWithHeader) {
        itemView.tvmHeaderLetter.text = item.header.toString()
    }

    companion object {

        fun newInstance(parent: ViewGroup) = HeaderViewHolder(parent.inflate(R.layout.item_region_header))
    }
}
