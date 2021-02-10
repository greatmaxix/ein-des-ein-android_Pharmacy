package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.pulse.R
import com.pulse.components.product.model.Product
import com.pulse.core.extensions.*
import com.pulse.databinding.LayoutRecentlyViewedItemBinding

class RecentlyViewedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding = LayoutRecentlyViewedItemBinding.inflate(inflater, this)

    private val cornerRadius by lazyGetDimension(R.dimen._7sdp)

    init {
        setCardBackgroundColor(getColor(R.color.colorGlobalWhite))
        radius = 0F
        cardElevation = resources.getDimensionPixelSize(R.dimen._1sdp).toFloat()
        useCompatPadding = true
        setRippleColorResource(R.color.colorRippleBlue)
        setBottomRoundCornerBackground(cornerRadius)
    }

    fun setProduct(product: Product) = with(binding) {
        ivRecentlyViewed.setProductImage(product.pictures, product.aggregation == null, false, R.drawable.bg_product_bottom_corners_background)
        ivRecentlyViewed.setBottomRoundCornerBackground(cornerRadius)
        mtvName.setTextHtml(product.rusName)
        mtvDescription.setTextHtml(product.releaseForm)
        val colorResId = if (product.isAggregationEmpty) R.color.greyText else R.color.darkBlue
        mtvDescription.setTextColorRes(colorResId)
        mtvName.setTextColorRes(colorResId)
        product.aggregation?.let {
            mtvPriceFrom.text = context.getString(R.string.price, it.minPrice.toString())
        } ?: run {
            mtvPriceFromPrefix.gone()
            mtvPriceUnavailable.visible()
        }
    }
}