package com.pulse.region.adapter.viewHolder

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.animateVisible
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.onClick
import com.pulse.model.region.Region
import com.pulse.model.region.RegionWithHeader
import kotlinx.android.synthetic.main.item_region.view.*

class RegionViewHolder(view: View, private val itemClick: (Region) -> Unit) : BaseViewHolder<RegionWithHeader>(view) {

    override fun bind(item: RegionWithHeader) {
        with(itemView) {
            mtvRegionName.text = item.region?.name
            ivRegionCheck.gone()
            onClick {
                ivRegionCheck.animateVisible()
                (ivRegionCheck.drawable as AnimatedVectorDrawable).start()
                item.region?.let(itemClick)
            }
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup, itemClick: (Region) -> Unit) = RegionViewHolder(parent.inflate(R.layout.item_region), itemClick)
    }
}
