package com.pulse.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.CornerFamily
import com.pulse.R
import com.pulse.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import kotlinx.android.synthetic.main.item_chat_product.view.*

class ProductViewHolder(itemView: View, private val productClickListener: (Action, Pair<MessageItem, Int>) -> Unit) : BaseViewHolder<MessageItem>(itemView) {

    init {
        itemView.setDebounceOnClickListener {
            val item = tag as MessageItem?
            if (item?.product != null) {
                productClickListener(Action.ITEM, item to item.product.globalProductId)
            }
        }
    }

    override fun bind(item: MessageItem) {
        with(itemView) {
            tag = item
//            tvChatProductRecipe.text = "Рецепт" // TODO set value and make visible
            item.product?.let {
                tvChatProductDescription.setTextHtml(item.product?.releaseForm)
                tvChatProductTitle.setTextHtml(item.product?.rusName)
                it.pharmacyProductsAggregationData?.let {
                    tvChatProductPrice.text = context.getString(R.string.price, it.minPrice.toString())
                }
                ivChatProduct.setProductImage(it)
                ivWish.setDebounceOnClickListener(2000) {
                    productClickListener(Action.WISH, item to it.globalProductId)
                }
                notifyHeart(it.isInWish)
            }
        }
    }

    fun notifyHeart(isInWish: Boolean) = itemView.ivWish.setWish(isInWish)

    enum class Action {
        ITEM,
        WISH
    }

    companion object {

        fun newInstance(parent: ViewGroup, productClickListener: (Action, Pair<MessageItem, Int>) -> Unit) =
            ProductViewHolder(parent.inflate(R.layout.item_chat_product), productClickListener)
    }
}