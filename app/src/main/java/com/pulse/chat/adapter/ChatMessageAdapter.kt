package com.pulse.chat.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pulse.chat.adapter.viewHolder.*
import com.pulse.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.falseIfNull

class ChatMessageAdapter(
    private val listener: (Action) -> Unit,
    private val productClickListener: (ProductViewHolder.Action, Pair<MessageItem, Int>) -> Unit
) : PagingDataAdapter<MessageItem, BaseViewHolder<MessageItem>>(ChatMessagesDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_MESSAGE_USER -> UserMessageViewHolder.newInstance(parent)
            TYPE_MESSAGE_PHARMACY -> PharmacyMessageViewHolder.newInstance(parent)
            TYPE_DATE_HEADER -> DateHeaderViewHolder.newInstance(parent)
            TYPE_ATTACHMENT -> AttachmentViewHolder.newInstance(parent)
            TYPE_PRODUCT -> ProductViewHolder.newInstance(parent, productClickListener)
            TYPE_AUTH_BUTTON -> AuthorizeButtonViewHolder.newInstance(parent, listener)
            else -> EndChatViewHolder.newInstance(parent, listener)
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
    }

    enum class Action {
        END_CHAT,
        RESUME_CHAT,
        AUTHORIZE
    }
}