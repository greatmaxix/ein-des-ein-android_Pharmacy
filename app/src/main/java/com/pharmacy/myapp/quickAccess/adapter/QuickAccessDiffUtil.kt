package com.pharmacy.myapp.quickAccess.adapter

import androidx.recyclerview.widget.DiffUtil

class QuickAccessDiffUtil(private val old: List<String>, private val new: List<String>) : DiffUtil.Callback() {

    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = old[oldItemPosition] == new[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true

}