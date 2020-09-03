package com.pharmacy.myapp.region.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.myapp.model.region.RegionWithHeader

class RegionDiff(private val oldList: List<RegionWithHeader>, private val newList: List<RegionWithHeader>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].region?.id == newList[newItemPosition].region?.id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}