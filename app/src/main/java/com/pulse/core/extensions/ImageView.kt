package com.pulse.core.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.pulse.R
import com.pulse.chat.model.message.MessageProduct
import com.pulse.components.cart.model.CartProduct
import com.pulse.model.Picture
import com.pulse.util.ColorFilterUtil

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

fun ImageView.loadGlide(drawableName: String) {
    Glide.with(this)
        .load(resources.getIdentifier(drawableName, "drawable", context.packageName))
        .into(this)
}

fun ImageView.loadGlideOrder(product: CartProduct) {
    visible()
    product.firstPictureUrl?.let {
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

// TODO refactor this
fun ImageView.setProductImage(product: MessageProduct) {
    loadGlide(product.pictures.firstOrNull()?.url) {
        transform(CenterCrop(), RoundedCorners(resources.getDimensionPixelSize(R.dimen._8sdp)))
        error(R.drawable.default_product_image)
    }
    val hasPictures = product.pictures.isNotEmpty()
    setBackgroundColor(if (hasPictures) 0 else ContextCompat.getColor(context, R.color.mediumGrey50))
    colorFilter = (if (product.pharmacyProductsAggregationData == null && !hasPictures) ColorFilterUtil.blackWhiteFilter else null)
}