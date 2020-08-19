package com.pharmacy.myapp.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pharmacy.myapp.R
import com.pharmacy.myapp.chat.model.ChatMessage
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.createGlide
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.resources
import com.pharmacy.myapp.core.extensions.visibleOrGone
import kotlinx.android.synthetic.main.item_chat_attachment.view.*

class AttachmentViewHolder(itemView: View) : BaseViewHolder<ChatMessage>(itemView) {

    private val radius = resources.getDimension(R.dimen._8sdp).toInt()
    private val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .priority(Priority.HIGH)
        .format(DecodeFormat.PREFER_ARGB_8888)
        .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))

    private val imageViewList = arrayListOf<ImageView>()

    init {
        with(itemView) {
            imageViewList.add(ivAttachment1)
            imageViewList.add(ivAttachment2)
            imageViewList.add(ivAttachment3)
            imageViewList.add(ivAttachment4)
            imageViewList.add(ivAttachment5)
            imageViewList.add(ivAttachment6)
        }
    }

    override fun bind(item: ChatMessage) {
        val items = item.asAttachment().items
        items.forEachIndexed { index, url ->
            imageViewList.getOrNull(index)?.let {
                it.createGlide
                    .load(url)
                    .apply(options)
                    .into(it)
            }
        }
        with(itemView) {
            ivAttachment2.visibleOrGone(items.size > 1)
            ivAttachment3.visibleOrGone(items.size > 2)
            ivAttachment4.visibleOrGone(items.size > 3)
            ivAttachment5.visibleOrGone(items.size > 4)
            ivAttachment6.visibleOrGone(items.size > 5)
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = AttachmentViewHolder(parent.inflate(R.layout.item_chat_attachment))
    }
}