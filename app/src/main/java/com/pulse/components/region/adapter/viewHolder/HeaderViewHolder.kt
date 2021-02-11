package com.pulse.components.region.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.databinding.ItemRegionHeaderBinding
import com.pulse.model.region.RegionWithHeader

class HeaderViewHolder(view: View) : BaseViewHolder<RegionWithHeader>(view) {

    private val binding by viewBinding(ItemRegionHeaderBinding::bind)

    override fun bind(item: RegionWithHeader) {
        binding.mtvLetter.text = item.header.toString()
    }

    companion object {

        fun newInstance(parent: ViewGroup) = HeaderViewHolder(parent.inflate(R.layout.item_region_header))
    }
}
