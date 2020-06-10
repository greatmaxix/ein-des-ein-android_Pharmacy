package com.pharmacy.myapp.core.extensions

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

@ColorInt
fun ViewHolder.compatColor(@ColorRes colorRes: Int) = context.getCompatColor(colorRes)

fun ViewHolder.dimensionPixelSize(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)

fun ViewHolder.dimension(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun ViewHolder.drawable(@DrawableRes drawableRes: Int) = context.getDrawable(drawableRes)

val ViewHolder.context: Context get() = itemView.context

val ViewHolder.resources: Resources get() = context.resources

fun ViewHolder.stringRes(@StringRes resId: Int) = context.getString(resId)

fun ViewHolder.stringRes(@StringRes resId: Int, vararg args: Any?) = context.getString(resId, *args)

fun <T : View> ViewHolder.findView(@IdRes resId: Int) = itemView.findViewById<T?>(resId)

fun ViewHolder.setParams(newWidth: Int? = null, newHeight: Int? = null, rvParams: ((RecyclerView.LayoutParams) -> Unit)? = null) {
    itemView.layoutParams = (itemView.layoutParams as RecyclerView.LayoutParams).apply {
        newWidth?.let { width = it }
        newHeight?.let { height = it }
        rvParams?.invoke(this)
    }
}