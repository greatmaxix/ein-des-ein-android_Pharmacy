package com.pharmacy.myapp.region.adapter

import android.view.ViewGroup
import com.pharmacy.myapp.core.base.adapter.BaseFilterRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.model.region.Region
import com.pharmacy.myapp.model.region.RegionWithHeader
import com.pharmacy.myapp.region.adapter.viewHolder.HeaderViewHolder
import com.pharmacy.myapp.region.adapter.viewHolder.RegionViewHolder

class RegionAdapter(private val itemClick: (Region) -> Unit) : BaseFilterRecyclerAdapter<RegionWithHeader, BaseViewHolder<RegionWithHeader>>() {

    override fun transformList(list: MutableList<RegionWithHeader>) = list
        .subList(0, (list.indexOfLast { it.region != null } + 1)) // skip all lasts headers without region
        .filterIndexed { index, item -> // filter intermediate header without region
            if (index != 0) !(item.header != 0.toChar() && list[index - 1].header != 0.toChar()) else true
        }
        .toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        HEADER -> HeaderViewHolder.newInstance(parent)
        else -> RegionViewHolder.newInstance(parent, itemClick)
    }

    override fun getItemViewType(position: Int) = if (getItem(position).region == null) HEADER else ITEM

    companion object {
        private const val HEADER = 0
        private const val ITEM = 1
    }

    override fun diffResult(origin: List<RegionWithHeader>, new: List<RegionWithHeader>) = RegionDiff(origin, new)

}