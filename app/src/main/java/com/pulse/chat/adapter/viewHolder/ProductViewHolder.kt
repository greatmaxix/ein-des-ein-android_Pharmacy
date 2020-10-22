package com.pulse.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.CornerFamily
import com.pulse.R
import com.pulse.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
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
//            tvChatProductRecipe.text = "Рецепт" // TODO set value and make visible
            tvChatProductDescription.setTextHtml(item.product?.releaseForm)
            item.product?.pharmacyProductsAggregationData?.let {
                tvChatProductPrice.text = context.getString(R.string.price, it.minPrice.toString())
            }
            tvChatProductTitle.setTextHtml(item.product?.rusName)
            item.product?.let(ivChatProduct::setProductImage)

            fabAddToCartChatProduct.onClick { itemView.context.toast("TODO: Add to cart") } // TODO add to cart
            ivWish.onClick { itemView.context.toast("TODO: Wish") } // TODO add to wish
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ProductViewHolder(parent.inflate(R.layout.item_chat_product))
    }
}