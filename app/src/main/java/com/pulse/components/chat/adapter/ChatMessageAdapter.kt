package com.pulse.components.chat.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pulse.components.chat.adapter.viewHolder.*
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.extensions.onClickDebounce

class ChatMessageAdapter(
    private val authClickListener: (Action) -> Unit,
    private val productClickListener: (ProductViewHolder.Action, Pair<MessageItem, Int>) -> Unit
) : PagingDataAdapter<MessageItem, BaseViewHolder<MessageItem>>(ChatMessagesDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_MESSAGE_USER -> UserMessageViewHolder.newInstance(parent)
            TYPE_MESSAGE_PHARMACY -> PharmacyMessageViewHolder.newInstance(parent)
            TYPE_DATE_HEADER -> DateHeaderViewHolder.newInstance(parent)
            TYPE_ATTACHMENT -> AttachmentViewHolder.newInstance(parent)
            TYPE_PRODUCT -> ProductViewHolder.newInstance(parent, productClickListener).apply {
                itemView.onClickDebounce {
                    val item = getItem(bindingAdapterPosition)
                    if (item?.product != null) productClickListener(ProductViewHolder.Action.ITEM, item to item.product.globalProductId)
                }
            }
            TYPE_AUTH_BUTTON -> AuthorizeButtonViewHolder.newInstance(parent, authClickListener)
            TYPE_RECIPE -> RecipeViewHolder.newInstance(parent)
            else -> EndChatViewHolder.newInstance(parent, authClickListener)
        }

    override fun getItemViewType(position: Int) = getItem(position)?.messageType ?: -1

    override fun onBindViewHolder(holder: BaseViewHolder<MessageItem>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MessageItem>, position: Int, payloads: MutableList<Any>) {
        if (holder is ProductViewHolder && payloads.isNotEmpty()) {
            val payload = payloads[0]
            if (payload is Boolean) {
                holder.notifyHeart(payload)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    fun notifyWish(globalProductId: Int) {
        val inWish = snapshot().find { it?.product?.globalProductId == globalProductId }?.product?.isInWish.falseIfNull()
        snapshot().forEachIndexed { position, messageItem ->
            messageItem?.let {
                if (it.product?.globalProductId == globalProductId) {
                    it.product.wish = !inWish
                    notifyItemChanged(position, it.product.isInWish)
                }
            }
        }
    }

    companion object {

        const val TYPE_MESSAGE_USER = 1
        const val TYPE_MESSAGE_PHARMACY = 2
        const val TYPE_DATE_HEADER = 3
        const val TYPE_ATTACHMENT = 4
        const val TYPE_PRODUCT = 5
        const val TYPE_AUTH_BUTTON = 6
        const val TYPE_END_CHAT = 7
        const val TYPE_RECIPE = 8
    }

    enum class Action {
        END_CHAT,
        RESUME_CHAT,
        AUTHORIZE
    }
}