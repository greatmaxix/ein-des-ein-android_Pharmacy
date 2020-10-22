package com.pulse.categories.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.onClick
import com.pulse.model.category.Category
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