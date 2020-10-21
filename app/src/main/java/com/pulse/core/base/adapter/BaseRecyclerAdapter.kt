package com.pulse.core.base.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<Type, ViewHolder : BaseViewHolder<Type>>(
    list: List<Type> = mutableListOf(),
    private val needNotifyList: Boolean = true
) : RecyclerView.Adapter<ViewHolder>() {

    open var items: MutableList<Type> = list.toMutableList()
        protected set(value) {
            field = value
            if (needNotifyList) {
                notifyDataSetChanged()
            }
        }

    protected var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    fun getItem(position: Int) = items[position]

    fun setItem(position: Int, item: Type) {
        items[position] = item
    }

    fun isEmpty() = itemCount == 0

    protected fun scrollTop() {
        recyclerView?.scrollToPosition(0)
    }
}