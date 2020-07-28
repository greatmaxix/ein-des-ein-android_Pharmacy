package com.pharmacy.myapp.productCard.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder

class ProductCardImageAdapter : BaseRecyclerAdapter<String, ProductCardImageAdapter.ImageViewHolder>() {

    fun setList(list: MutableList<String>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder = ImageViewHolder.newInstance(parent)

    class ImageViewHolder(view: View) : BaseViewHolder<String>(view) {

        override fun bind(item: String) {
            val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .priority(Priority.HIGH)
                .format(DecodeFormat.PREFER_ARGB_8888)

            Glide.with(itemView)
                .applyDefaultRequestOptions(options)
                .load(item)
                .into(itemView as ImageView)
        }

        companion object {

            fun newInstance(parent: ViewGroup) = ImageViewHolder(
                ImageView(parent.context)
                    .apply {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    }
            )
        }
    }
}