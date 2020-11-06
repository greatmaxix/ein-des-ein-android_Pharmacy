package com.pulse.productList.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.product.model.ProductLite
import kotlinx.android.synthetic.main.item_product.view.*

class ProductListViewHolder(override val containerView: View, private val wishClick: (Pair<Boolean, Int>) -> Unit) : BaseViewHolder<ProductLite>(containerView) {

    override fun bind(item: ProductLite) = with(itemView) {

        ivProduct.setProductImage(item.pictures, item.isAggregationEmpty)

        tvTitle.setTextHtml(item.rusName)
        tvSubTitle.setTextHtml(item.releaseForm)

        tvManufacture.setTextHtml(stringRes(R.string.manufacture, item.productLocale))

        item.aggregation?.let {
            tvProductPrice.text = stringRes(R.string.price, it.minPrice.formatPrice())
            tvProductPrice.visible()
        } ?: run { tvProductPrice.gone() }
        tvPricePrefix.visibleOrGone(!item.isAggregationEmpty)
        tvPriceUnavailable.visibleOrGone(item.isAggregationEmpty)
        val colorResId = if (item.isAggregationEmpty) R.color.greyText else R.color.darkBlue
        tvTitle.textColor(colorResId)
        tvSubTitle.textColor(colorResId)
        tvManufacture.textColor(colorResId)

        with(ivWish) {
            setDebounceOnClickListener(2000) { wishClick(!item.isInWish to item.globalProductId) }
            notifyHeart(item.isInWish)
        }
    }

    fun notifyHeart(isInWish: Boolean) = itemView.ivWish.setWish(isInWish)

    companion object {
        fun newInstance(parent: ViewGroup, wishClick: (Pair<Boolean, Int>) -> Unit) = ProductListViewHolder(parent.inflate(R.layout.item_product), wishClick)
    }
}