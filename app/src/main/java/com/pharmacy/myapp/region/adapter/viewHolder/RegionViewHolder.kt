package com.pharmacy.myapp.region.adapter.viewHolder

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.animateVisible
import com.pharmacy.myapp.core.extensions.gone
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.model.region.Region
import com.pharmacy.myapp.model.region.RegionWithHeader
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
