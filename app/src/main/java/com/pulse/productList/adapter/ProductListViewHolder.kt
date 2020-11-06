package com.pulse.productList.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.product.model.ProductLite
import kotlinx.android.synthetic.main.item_product.view.*

class ProductListViewHolder(override val containerView: View, private val wishClick: (Pair<Boolean, Int>) -> Unit) : BaseViewHolder<ProductLite>(containerView) {

    override fun bind(item: ProductLite) = with(item) {

        itemView.ivProduct.setProductImage(pictures, isAggregationEmpty)

        itemView.tvTitle.setTextHtml(rusName)
        itemView.tvSubTitle.setTextHtml(releaseForm)

        itemView.tvManufacture.setTextHtml(stringRes(R.string.manufacture, productLocale))

        aggregation?.let {
            itemView.tvProductPrice.text = stringRes(R.string.price, it.minPrice.formatPrice())
            itemView.tvProductPrice.visible()
        } ?: run { itemView.tvProductPrice.gone() }
        itemView.tvPricePrefix.visibleOrGone(aggregation != null)
        itemView.tvPriceUnavailable.visibleOrGone(isAggregationEmpty)
        val colorResId = if (isAggregationEmpty) R.color.greyText else R.color.darkBlue
        itemView.tvTitle.textColor(colorResId)
        itemView.tvSubTitle.textColor(colorResId)
        itemView.tvManufacture.textColor(colorResId)

        with(itemView.ivWish) {
            setDebounceOnClickListener(2000) { wishClick(!isInWish to globalProductId) }
            notifyHeart(isInWish)
        }
    }

    fun notifyHeart(isInWish: Boolean) = itemView.ivWish.setWish(isInWish)

    companion object {
        fun newInstance(parent: ViewGroup, wishClick: (Pair<Boolean, Int>) -> Unit) = ProductListViewHolder(parent.inflate(R.layout.item_product), wishClick)
    }
}