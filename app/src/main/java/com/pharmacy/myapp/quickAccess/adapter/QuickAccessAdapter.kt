package com.pharmacy.myapp.quickAccess.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.extensions.onClick
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.withContext

class QuickAccessAdapter(private val click: (String) -> Unit) : BaseRecyclerAdapter<String, QuickAccessViewHolder>(needNotifyList = false) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = QuickAccessViewHolder.newInstance(parent).apply {
        itemView.onClick { click(getItem(bindingAdapterPosition)) }
    }

    suspend fun notifyItems(newItems: MutableList<String>) {
        val diff = withContext(Default) { DiffUtil.calculateDiff(QuickAccessDiffUtil(items, newItems)) }
        items = newItems
        diff.dispatchUpdatesTo(this)
        scrollTop()
    }
}