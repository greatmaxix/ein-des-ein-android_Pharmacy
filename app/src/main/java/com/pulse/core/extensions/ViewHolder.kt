package com.pulse.core.extensions

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.*
import androidx.recyclerview.widget.RecyclerView.ViewHolder

val ViewHolder.context: Context get() = itemView.context

val ViewHolder.resources: Resources get() = context.resources

@ColorInt
fun ViewHolder.color(@ColorRes colorRes: Int) = context.getColor(colorRes)

fun ViewHolder.dimension(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun ViewHolder.dimensionPixelSize(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)

fun ViewHolder.drawable(@DrawableRes drawableRes: Int) = context.getCompatDrawable(drawableRes)

fun ViewHolder.stringRes(@StringRes resId: Int) = context.getString(resId)

fun ViewHolder.stringRes(@StringRes resId: Int, vararg args: Any?) = context.getString(resId, *args)

fun <T : View> ViewHolder.findView(@IdRes resId: Int): T? = itemView.findViewById(resId)