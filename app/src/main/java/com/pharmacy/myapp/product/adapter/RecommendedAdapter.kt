package com.pharmacy.myapp.product.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.loadGlide
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.toast
import com.pharmacy.myapp.product.model.TempRecommendedModel
import kotlinx.android.synthetic.main.item_recommended.view.*

class RecommendedAdapter : BaseRecyclerAdapter<TempRecommendedModel, RecommendedAdapter.RecommendedViewHolder>() {

    fun setList(list: MutableList<TempRecommendedModel>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder = RecommendedViewHolder.newInstance(parent)

    class RecommendedViewHolder(view: View) : BaseViewHolder<TempRecommendedModel>(view) {

        override fun bind(item: TempRecommendedModel) {
            itemView.recommendedImage.loadGlide(item.imageUrl)

            itemView.recommendedFavorite.onClick { itemView.context.toast("TODO: Favorite") }
            itemView.recommendedTitle.text = item.name
            itemView.recommendedDescription.text = item.description
            itemView.recommendedAddToCartFab.onClick { itemView.context.toast("TODO: Add to cart") }
            itemView.recommendedPrice.text = item.price
            itemView.recommendedRecipe.text = item.recipeTitle
        }

        companion object {

            fun newInstance(parent: ViewGroup) = RecommendedViewHolder(parent.inflate(R.layout.item_recommended))
        }
    }
}