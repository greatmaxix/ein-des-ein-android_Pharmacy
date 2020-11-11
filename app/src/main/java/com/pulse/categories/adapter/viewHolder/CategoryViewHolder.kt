package com.pulse.categories.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.visible
import com.pulse.model.category.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryViewHolder(view: View) : BaseViewHolder<Category>(view) {

    override fun bind(item: Category) = with(itemView) {
        tvCategoryNameTile.text = item.name
        if (item.drawableName != -1) {
            ivCategoryIconTile.setImageResource(item.drawableName)
            ivCategoryIconTile.visible()
        } else {
            ivCategoryIconTile.gone()
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup) = CategoryViewHolder(parent.inflate(R.layout.item_category))
    }
}