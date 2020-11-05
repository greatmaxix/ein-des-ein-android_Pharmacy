package com.pulse.categories.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.model.category.Category
import kotlinx.android.synthetic.main.item_category_tile.view.*

class CategoryTileViewHolder(view: View) : BaseViewHolder<Category>(view) {

    override fun bind(item: Category) {
        itemView.tvCategoryNameTile.text = item.name

        if (item.drawableName != -1)
            itemView.ivCategoryIconTile.setImageResource(item.drawableName)
    }

    companion object {
        fun newInstance(parent: ViewGroup) = CategoryTileViewHolder(parent.inflate(R.layout.item_category_tile))
    }
}