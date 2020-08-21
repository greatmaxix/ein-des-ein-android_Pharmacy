package com.pharmacy.myapp.search.adapter

import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.view.View
import android.view.ViewGroup
import android.widget.TextView.BufferType.SPANNABLE
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.loadGlide
import com.pharmacy.myapp.core.extensions.stringRes
import com.pharmacy.myapp.model.product.Product
import kotlinx.android.synthetic.main.item_search.view.*

class SearchViewHolder(override val containerView: View) : BaseViewHolder<Product>(containerView) {

    override fun bind(item: Product) = with(item) {
        if (pictures.isNotEmpty()) {
            itemView.ivProduct.loadGlide(pictures.first().url) { transform(CenterCrop(), RoundedCorners(R.dimen._8sdp.toPixelSize)) }
        }

        itemView.tvTitle.setText(Html.fromHtml(rusName, FROM_HTML_MODE_COMPACT), SPANNABLE)
        itemView.tvSubTitle.setText(Html.fromHtml(releaseForm, FROM_HTML_MODE_COMPACT), SPANNABLE)

        itemView.tvPrice.text = stringRes(R.string.price, aggregation.minPrice)
    }

    companion object {
        fun newInstance(parent: ViewGroup) = SearchViewHolder(parent.inflate(R.layout.item_search))
    }
}