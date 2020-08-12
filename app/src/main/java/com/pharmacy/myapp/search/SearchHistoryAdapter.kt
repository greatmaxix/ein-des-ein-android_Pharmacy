package com.pharmacy.myapp.search

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.onClick
import kotlinx.android.synthetic.main.item_history_search.view.*

class SearchHistoryAdapter : BaseRecyclerAdapter<String, SearchHistoryAdapter.SearchHistoryViewHolder>() {

    private var itemClick:((String) -> Unit)? = null

    fun setList(list: MutableList<String>) {
        items = list
    }

    fun setClickListener(listener:(String) -> Unit) {
        itemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder = SearchHistoryViewHolder.newInstance(parent).apply {
        itemView.onClick {
            itemClick?.invoke(getItem(adapterPosition))
        }
    }

    class SearchHistoryViewHolder(view: View) : BaseViewHolder<String>(view) {

        override fun bind(item: String) {
            itemView.mtvTitleHistorySearchItem.text = item
        }

        companion object {

            fun newInstance(parent: ViewGroup) = SearchHistoryViewHolder(parent.inflate(R.layout.item_history_search))
        }
    }
}