package com.pharmacy.myapp.core.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.pharmacy.myapp.R
import com.pharmacy.myapp.cart.model.CartProduct
import com.pharmacy.myapp.model.Picture
import com.pharmacy.myapp.util.ColorFilterUtil

fun ImageView.setFilter(@ColorInt color: Int) {
    drawable?.setFilter(color)
}

fun ImageView.setFilterRes(@ColorRes colorRes: Int) {
    val color = context.getColor(colorRes)
    setFilter(color)
}

val ImageView.createGlide
    get() = Glide.with(this)

fun ImageView.loadGlide(url: String?, block: (RequestBuilder<Drawable>.() -> Unit)? = null) {
    val glide = createGlide.load(url)
    block?.let { glide.apply(it).into(this) } ?: glide.into(this)
}

fun ImageView.loadGlideOrder(product: CartProduct) {
    product.pictures.firstOrNull()?.url?.let {
        val options: RequestBuilder<Drawable>.() -> Unit = {
            transition(DrawableTransitionOptions.withCrossFade())
            override(300)
            transform(MultiTransformation(CenterCrop(), RoundedCorners(resources.getDimensionPixelSize(R.dimen._6sdp))))
        }
        loadGlide(it, options)
    }
}

fun ImageView.loadGlideDrugstore(url: String?) = loadGlide(url) {
    placeholder(R.drawable.ic_drugstore_base)
    RequestOptions.bitmapTransform(CircleCrop())
    transition(DrawableTransitionOptions().crossFade())
}

fun ImageView.setWish(isWish: Boolean) = setImageResource(isWish.wishResId)

fun ImageView.setProductImage(list: List<Picture>, hasAggregation: Boolean = false) {
    loadGlide(list.firstOrNull()?.url) {
        transform(CenterCrop(), RoundedCorners(resources.getDimensionPixelSize(R.dimen._8sdp)))
        error(R.drawable.default_product_image)
    }
    val hasPictures = list.isNotEmpty()
    background = if (hasPictures) null else context.getCompatDrawable(R.drawable.bg_product_default_background)
    colorFilter = (if (hasAggregation && !hasPictures) ColorFilterUtil.blackWhiteFilter else null)
}