package com.pulse.components.productList.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.product.model.ProductLite
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.databinding.ItemProductBinding

class ProductListViewHolder(override val containerView: View, private val wishClick: (Pair<Boolean, Int>) -> Unit) : BaseViewHolder<ProductLite>(containerView) {

    private val binding by viewBinding(ItemProductBinding::bind)

    override fun bind(item: ProductLite) = with(binding) {
        ivProduct.setProductImage(item.pictures, item.isAggregationEmpty)
        mtvTitle.setTextHtml(item.rusName)
        mtvSubtitle.setTextHtml(item.releaseForm)
        mtvManufacture.setTextHtml(getString(R.string.manufacture, item.productLocale))

        item.aggregation?.let {
            mtvProductPrice.text = getString(R.string.price, it.minPrice.formatPrice())
            mtvProductPrice.visible()
        } ?: run { mtvProductPrice.gone() }
        mtvPricePrefix.visibleOrGone(!item.isAggregationEmpty)
        mtvPriceUnavailable.visibleOrGone(item.isAggregationEmpty)
        val colorResId = if (item.isAggregationEmpty) R.color.greyText else R.color.darkBlue
        mtvTitle.setTextColorRes(colorResId)
        mtvSubtitle.setTextColorRes(colorResId)
        mtvManufacture.setTextColorRes(colorResId)

        with(ivWish) {
            setDebounceOnClickListener(2000) { wishClick(!item.isInWish to item.globalProductId) }
            notifyHeart(item.isInWish)
        }
    }

    fun notifyHeart(isInWish: Boolean) = binding.ivWish.setWish(isInWish)

    companion object {

        fun newInstance(parent: ViewGroup, wishClick: (Pair<Boolean, Int>) -> Unit) = ProductListViewHolder(parent.inflate(R.layout.item_product), wishClick)
    }
}