package com.pharmacy.categories.adapter

import android.view.ViewGroup
import com.pharmacy.categories.adapter.viewHolder.CategoryTileViewHolder
import com.pharmacy.core.base.adapter.BaseFilterRecyclerAdapter
import com.pharmacy.model.category.Category

class CategoriesAdapter(private val click :(Category) -> Unit) : BaseFilterRecyclerAdapter<Category, CategoryTileViewHolder>() {

    override fun diffResult(origin: List<Category>, new: List<Category>) = CategoryDiff(origin, new)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryTileViewHolder.newInstance(parent, click)

}