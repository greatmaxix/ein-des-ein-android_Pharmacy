package com.pharmacy.myapp.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.CornerFamily
import com.pharmacy.myapp.R
import com.pharmacy.myapp.chat.model.message.MessageItem
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.*
import kotlinx.android.synthetic.main.item_chat_product.view.*

class ProductViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    init {
        val radius = resources.getDimension(R.dimen._8sdp)
        itemView.cardChatProduct.shapeAppearanceModel = itemView.cardChatProduct.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .build()
    }

    override fun bind(item: MessageItem) {
        with(itemView) {
            tvChatProductRecipe.text = "Рецепт" // TODO
            tvChatProductDescription.setTextHtml(item.product?.releaseForm)
            item.product?.pharmacyProductsAggregationData?.let {
                tvChatProductPrice.text = context.getString(R.string.price, it.minPrice.toString())
            }
            tvChatProductTitle.setTextHtml(item.product?.rusName)
            item.product?.pictures?.firstOrNull()?.url?.let(ivChatProduct::loadGlide)

            fabAddToCartChatProduct.onClick { itemView.context.toast("TODO: Add to cart") } // TODO add to cart
            ivWish.onClick { itemView.context.toast("TODO: Wish") } // TODO add to wish
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ProductViewHolder(parent.inflate(R.layout.item_chat_product))
    }
}