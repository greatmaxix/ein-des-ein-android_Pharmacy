package com.pharmacy.categories.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.R
import com.pharmacy.core.base.adapter.BaseViewHolder
import com.pharmacy.core.extensions.inflate
import com.pharmacy.core.extensions.onClick
import com.pharmacy.model.category.Category
import kotlinx.android.synthetic.main.item_category_tile.view.*

class CategoryTileViewHolder(view: View, val click: (Category) -> Unit) : BaseViewHolder<Category>(view) {
    override fun bind(item: Category) {
        itemView.tvCategoryNameTile.text = item.name
        itemView.mcvCategoryTile.onClick { click(item) }
    }

    companion object {

        fun newInstance(parent: ViewGroup, click: (Category) -> Unit) = CategoryTileViewHolder(parent.inflate(R.layout.item_category_tile), click)
    }
}