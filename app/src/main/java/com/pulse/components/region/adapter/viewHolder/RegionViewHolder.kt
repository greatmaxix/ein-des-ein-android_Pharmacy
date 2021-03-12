package com.pulse.components.region.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.animateVisible
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.ItemCheckableBinding
import com.pulse.model.region.Region
import com.pulse.model.region.RegionWithHeader

class RegionViewHolder(view: View, private val itemClick: (Region) -> Unit) : BaseViewHolder<RegionWithHeader>(view) {

    private val binding by viewBinding(ItemCheckableBinding::bind)

    override fun bind(item: RegionWithHeader) = with(binding) {
        mtvName.text = item.region?.name
        ivCheck.gone()
        root.setDebounceOnClickListener {
            ivCheck.animateVisible()
            item.region?.let(itemClick)
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup, itemClick: (Region) -> Unit) = RegionViewHolder(parent.inflate(R.layout.item_checkable), itemClick)
    }
}
