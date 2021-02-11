package com.pulse.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.databinding.ItemChatProductBinding

class ProductViewHolder(itemView: View, private val productClickListener: (Action, Pair<MessageItem, Int>) -> Unit) : BaseViewHolder<MessageItem>(itemView) {

    private val binding by viewBinding(ItemChatProductBinding::bind)

    override fun bind(item: MessageItem) {
        with(binding) {
//            tvChatProductRecipe.text = "Рецепт" // TODO set value and make visible
            item.product?.let {
                mtvDescription.setTextHtml(item.product?.releaseForm)
                mtvTitle.setTextHtml(item.product?.rusName)
                it.pharmacyProductsAggregationData?.let {
                    mtvPrice.text = context.getString(R.string.price, it.minPrice.toString())
                }
                ivChatProduct.setProductImage(it)
                ivWish.setDebounceOnClickListener(2000) {
                    productClickListener(Action.WISH, item to it.globalProductId)
                }
                notifyHeart(it.isInWish)
            }
        }
    }

    fun notifyHeart(isInWish: Boolean) = binding.ivWish.setWish(isInWish)

    enum class Action {
        ITEM,
        WISH
    }

    companion object {

        fun newInstance(parent: ViewGroup, productClickListener: (Action, Pair<MessageItem, Int>) -> Unit) =
            ProductViewHolder(parent.inflate(R.layout.item_chat_product), productClickListener)
    }
}