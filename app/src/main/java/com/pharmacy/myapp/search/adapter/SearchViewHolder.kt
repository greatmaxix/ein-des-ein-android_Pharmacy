package com.pharmacy.myapp.search.adapter

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.loadGlide
import com.pharmacy.myapp.core.extensions.setTextHtml
import com.pharmacy.myapp.core.extensions.stringRes
import com.pharmacy.myapp.product.model.Product
import kotlinx.android.synthetic.main.item_search.view.*

class SearchViewHolder(override val containerView: View) : BaseViewHolder<Product>(containerView) {

    override fun bind(item: Product) = with(item) {
        if (pictures.isNotEmpty()) {
            itemView.ivProduct.loadGlide(pictures.first().url) { transform(CenterCrop(), RoundedCorners(R.dimen._8sdp.toPixelSize)) }
        }

        itemView.tvTitle.setTextHtml(rusName)
        itemView.tvSubTitle.setTextHtml(releaseForm)

        itemView.tvPrice.text = stringRes(R.string.price, aggregation.minPrice)
    }

    companion object {
        fun newInstance(parent: ViewGroup) = SearchViewHolder(parent.inflate(R.layout.item_search))
    }
}