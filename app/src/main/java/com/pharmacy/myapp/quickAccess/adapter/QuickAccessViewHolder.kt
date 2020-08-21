package com.pharmacy.myapp.quickAccess.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import kotlinx.android.synthetic.main.item_quick_access.view.*

class QuickAccessViewHolder(override val containerView: View) : BaseViewHolder<String>(containerView) {

    override fun bind(item: String) {
        itemView.mtvQuickAccess.text = item
    }

    companion object {
        fun newInstance(parent: ViewGroup) = QuickAccessViewHolder(parent.inflate(R.layout.item_quick_access))
    }
}