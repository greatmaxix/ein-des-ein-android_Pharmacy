package com.pharmacy.myapp.categories.adapter

import android.view.ViewGroup
import com.pharmacy.myapp.categories.adapter.viewHolder.CategoryViewHolder
import com.pharmacy.myapp.core.base.adapter.BaseFilterRecyclerAdapter
import com.pharmacy.myapp.model.category.Category

class NestedCategoriesAdapter(list: MutableList<Category>, private val click: (Category) -> Unit) : BaseFilterRecyclerAdapter<Category, CategoryViewHolder>(list) {

    override fun diffResult(origin: List<Category>, new: List<Category>) = CategoryDiff(origin, new)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder.newInstance(parent, click)

}