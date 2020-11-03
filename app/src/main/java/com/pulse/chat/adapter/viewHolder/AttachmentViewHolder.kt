package com.pulse.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pulse.R
import com.pulse.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.loadGlideDrawableByURL
import com.pulse.core.extensions.resources
import kotlinx.android.synthetic.main.item_chat_attachment.view.*

class AttachmentViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val radius = resources.getDimension(R.dimen._8sdp).toInt()
    private val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .priority(Priority.HIGH)
        .format(DecodeFormat.PREFER_ARGB_8888)
        .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))

    override fun bind(item: MessageItem) {
        with(itemView) {
            ivAttachment.loadGlideDrawableByURL(item.file?.url) {
                apply(requestOptions)
            }
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = AttachmentViewHolder(parent.inflate(R.layout.item_chat_attachment))
    }
}