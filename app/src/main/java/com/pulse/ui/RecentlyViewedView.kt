package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.pulse.R
import com.pulse.core.extensions.*
import com.pulse.product.model.Product
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_recently_viewed_item.view.*

class RecentlyViewedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView = inflate(R.layout.layout_recently_viewed_item, true)

    private val cornerRadius by lazyDimension(R.dimen._7sdp)

    init {
        setCardBackgroundColor(getColor(R.color.colorGlobalWhite))
        radius = 0F
        cardElevation = resources.getDimensionPixelSize(R.dimen._1sdp).toFloat()
        useCompatPadding = true
        setRippleColorResource(R.color.colorRippleBlue)
        setBottomRoundCornerBackground(cornerRadius)
    }

    fun setProduct(product: Product) {
        ivRecentlyViewed.setProductImage(product.pictures, product.aggregation == null, false, R.drawable.bg_product_bottom_corners_background)
        ivRecentlyViewed.setBottomRoundCornerBackground(cornerRadius)
        tvNameRecentlyViewed.setTextHtml(product.rusName)
        tvDescriptionRecentlyViewed.setTextHtml(product.releaseForm)
        val colorResId = if (product.isAggregationEmpty) R.color.greyText else R.color.darkBlue
        tvDescriptionRecentlyViewed.setTextColorRes(colorResId)
        tvNameRecentlyViewed.setTextColorRes(colorResId)
        product.aggregation?.let {
            tvPriceFromRecentlyViewed.text = context.getString(R.string.price, it.minPrice.toString())
        } ?: run {
            tvPriceFromPrefixRecentlyViewed.gone()
            tvPriceUnavailableRecentlyViewed.visible()
        }
    }
}