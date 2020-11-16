package com.pulse.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import kotlinx.android.synthetic.main.item_chat_message_recipe.view.*

class RecipeViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    override fun bind(item: MessageItem) {
        itemView.tvRecipeNameChat.text = item.recipeImage?.originalFilename
    }

    companion object {

        fun newInstance(parent: ViewGroup) = RecipeViewHolder(parent.inflate(R.layout.item_chat_message_recipe))
    }
}