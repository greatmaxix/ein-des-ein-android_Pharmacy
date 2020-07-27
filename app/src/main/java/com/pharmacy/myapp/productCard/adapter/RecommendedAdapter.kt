package com.pharmacy.myapp.productCard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.toast
import com.pharmacy.myapp.productCard.model.TempRecommendedModel
import kotlinx.android.synthetic.main.item_recommended.view.*

class RecommendedAdapter(items: List<TempRecommendedModel>) :
    BaseRecyclerAdapter<TempRecommendedModel, RecommendedAdapter.RecommendedViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended, parent, false)
        return RecommendedViewHolder(view)
    }

    class RecommendedViewHolder(view: View) : BaseViewHolder<TempRecommendedModel>(view) {

        override fun bind(item: TempRecommendedModel) {
            Glide.with(itemView)
                .load(item.imageUrl)
                .into(itemView.recommendedImage)

            itemView.recommendedFavorite.onClick { itemView.context.toast("TODO: Favorite") }
            itemView.recommendedTitle.text = item.name
            itemView.recommendedDescription.text = item.description
            itemView.recommendedAddToCartFab.onClick { itemView.context.toast("TODO: Add to cart") }
            itemView.recommendedPrice.text = item.price
            itemView.recommendedRecipe.text = item.recipeTitle
        }
    }
}