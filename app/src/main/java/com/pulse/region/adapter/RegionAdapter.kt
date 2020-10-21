package com.pulse.region.adapter

import android.view.ViewGroup
import com.pulse.core.base.adapter.BaseFilterRecyclerAdapter
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.model.region.Region
import com.pulse.model.region.RegionWithHeader
import com.pulse.region.adapter.viewHolder.HeaderViewHolder
import com.pulse.region.adapter.viewHolder.RegionViewHolder

class RegionAdapter(private val itemClick: (Region) -> Unit, private val emptyListCallback: (Boolean) -> Unit) : BaseFilterRecyclerAdapter<RegionWithHeader, BaseViewHolder<RegionWithHeader>>() {

    override fun transformList(list: MutableList<RegionWithHeader>): MutableList<RegionWithHeader> {
        val transformedList = list
            .subList(0, (list.indexOfLast { it.region != null } + 1)) // skip all lasts headers without region
            .filterIndexed { index, item -> // filter intermediate header without region
                if (index != 0) !(item.header != 0.toChar() && list[index - 1].header != 0.toChar()) else true
            }
            .toMutableList()
        emptyListCallback(transformedList.isEmpty())
        return transformedList
    }

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