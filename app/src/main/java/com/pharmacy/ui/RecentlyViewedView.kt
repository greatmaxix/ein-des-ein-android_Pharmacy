package com.pharmacy.ui

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.pharmacy.R
import com.pharmacy.core.extensions.*
import com.pharmacy.product.model.Product
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_recently_viewed_item.view.*

class RecentlyViewedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView = inflate(R.layout.layout_recently_viewed_item, true)

    init {
        setCardBackgroundColor(colorCompat(R.color.colorGlobalWhite))
        radius = resources.getDimensionPixelSize(R.dimen._7sdp).toFloat()
        cardElevation = resources.getDimensionPixelSize(R.dimen._1sdp).toFloat()
        useCompatPadding = true
        setRippleColorResource(R.color.colorRippleBlue)
    }

    fun setProduct(product: Product) {
        ivRecentlyViewed.setProductImage(product.pictures, product.aggregation == null)
        tvNameRecentlyViewed.setTextHtml(product.rusName)
        tvDescriptionRecentlyViewed.setTextHtml(product.releaseForm)
        product.aggregation?.let {
            tvPriceFromRecentlyViewed.text = context.getString(R.string.price, it.minPrice.toString())
        } ?: run { tvPriceFromPrefixRecentlyViewed.gone() }
    }
}