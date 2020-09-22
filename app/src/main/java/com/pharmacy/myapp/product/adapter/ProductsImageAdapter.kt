package com.pharmacy.myapp.product.adapter

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.loadGlide
import com.pharmacy.myapp.model.Picture

class ProductsImageAdapter(items: List<Picture>) : BaseRecyclerAdapter<Picture, ProductsImageAdapter.ImageViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder = ImageViewHolder.newInstance(parent)

    class ImageViewHolder(view: View) : BaseViewHolder<Picture>(view) {

        override fun bind(item: Picture) = (itemView as ImageView).loadGlide(item.url)

        companion object {
            fun newInstance(parent: ViewGroup) = ImageViewHolder(ImageView(parent.context)
                .apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) })
        }
    }
}