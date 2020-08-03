package com.pharmacy.myapp.core.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

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