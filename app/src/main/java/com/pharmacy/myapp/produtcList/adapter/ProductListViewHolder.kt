package com.pharmacy.myapp.produtcList.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.product.model.ProductLite
import kotlinx.android.synthetic.main.item_product.view.*

class ProductListViewHolder(override val containerView: View, private val wishClick: (Pair<Boolean, Int>) -> Unit) : BaseViewHolder<ProductLite>(containerView) {

    val filter by lazy { ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) }) }

    override fun bind(item: ProductLite) = with(item) {

        if (pictures.isNotEmpty()) {
            itemView.ivProduct.loadGlide(pictures.first().url) {
                transform(CenterCrop(), RoundedCorners(R.dimen._8sdp.toPixelSize))
            }
            itemView.ivProduct.setBackgroundColor(0)
        } else {
            itemView.ivProduct.setBackgroundColor(compatColor(R.color.mediumGrey50))
            itemView.ivProduct.colorFilter = (if (aggregation == null) filter else null)
        }

        itemView.tvTitle.setTextHtml(rusName)
        itemView.tvSubTitle.setTextHtml(releaseForm)

        itemView.tvManufacture.setTextHtml(stringRes(R.string.manufacture, productLocale))

        aggregation?.let { itemView.tvProductPrice.text = stringRes(R.string.price, it.minPrice) }
        itemView.tvPricePrefix.visibleOrGone(aggregation != null)
        itemView.tvPriceUnavailable.visibleOrGone(aggregation == null)

        with(itemView.ivWish) {
            setDebounceOnClickListener(2000) { wishClick(!isWish to globalProductId) }
            notifyHeart(isWish)
        }
    }

    fun notifyHeart(isInWish: Boolean) = itemView.ivWish.setWish(isInWish)

    companion object {
        fun newInstance(parent: ViewGroup, wishClick: (Pair<Boolean, Int>) -> Unit) = ProductListViewHolder(parent.inflate(R.layout.item_product), wishClick)
    }
}