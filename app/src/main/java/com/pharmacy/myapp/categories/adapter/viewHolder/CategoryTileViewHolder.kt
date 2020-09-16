package com.pharmacy.myapp.categories.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.model.category.Category
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