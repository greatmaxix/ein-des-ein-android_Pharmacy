package com.pharmacy.productList.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.R
import com.pharmacy.core.base.adapter.BaseViewHolder
import com.pharmacy.core.extensions.*
import com.pharmacy.product.model.ProductLite
import kotlinx.android.synthetic.main.item_product.view.*

class ProductListViewHolder(override val containerView: View, private val wishClick: (Pair<Boolean, Int>) -> Unit) : BaseViewHolder<ProductLite>(containerView) {

    override fun bind(item: ProductLite) = with(item) {

        itemView.ivProduct.setProductImage(pictures, aggregation == null)

        itemView.tvTitle.setTextHtml(rusName)
        itemView.tvSubTitle.setTextHtml(releaseForm)

        itemView.tvManufacture.setTextHtml(stringRes(R.string.manufacture, productLocale))

        aggregation?.let {
            itemView.tvProductPrice.text = stringRes(R.string.price, it.minPrice.formatPrice())
            itemView.tvProductPrice.visible()
        } ?: run { itemView.tvProductPrice.gone() }
        itemView.tvPricePrefix.visibleOrGone(aggregation != null)
        itemView.tvPriceUnavailable.visibleOrGone(aggregation == null)

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