package com.pharmacy.myapp.produtcList

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
import com.pharmacy.myapp.product.model.ProductLite
import kotlinx.android.synthetic.main.item_product.view.*

class ProductListViewHolder(override val containerView: View, private val onClick: (Triple<Boolean, Int, Int>) -> Unit) : BaseViewHolder<ProductLite>(containerView) {

    override fun bind(item: ProductLite) = with(item) {

        if (pictures.isNotEmpty()) {
            itemView.ivProduct.loadGlide(pictures.first().url) {
                transform(CenterCrop(), RoundedCorners(R.dimen._8sdp.toPixelSize))
            }
        }

        itemView.tvTitle.setTextHtml(rusName)
        itemView.tvSubTitle.setTextHtml(releaseForm)

        itemView.tvManufacture.setTextHtml(stringRes(R.string.manufacture, manufacture.producerRu))
        itemView.tvPrice.text = stringRes(R.string.price, aggregation.minPrice)

        with(itemView.ivWish) {
            setOnClickListener {
                onClick(Triple(!isWish, globalProductId, bindingAdapterPosition))
            }
            notifyHeart(isWish)
        }
    }

    fun notifyHeart(isInWish: Boolean) {
        itemView.ivWish.setImageResource(if (isInWish) R.drawable.ic_heart_fill else R.drawable.ic_heart_stroke)
    }

    companion object {
        fun newInstance(parent: ViewGroup, onClick: (Triple<Boolean, Int, Int>) -> Unit) = ProductListViewHolder(parent.inflate(R.layout.item_product), onClick)
    }
}