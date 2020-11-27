package com.pulse.core.extensions

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.*
import androidx.recyclerview.widget.RecyclerView.ViewHolder

val ViewHolder.context: Context get() = itemView.context

val ViewHolder.resources: Resources get() = context.resources

@ColorInt
fun ViewHolder.getColor(@ColorRes resId: Int) = context.getColor(resId)

fun ViewHolder.getDimension(@DimenRes resId: Int) = resources.getDimension(resId)

fun ViewHolder.getDimensionPixelSize(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

fun ViewHolder.getDrawable(@DrawableRes resId: Int) = context.getCompatDrawable(resId)

fun ViewHolder.getString(@StringRes resId: Int) = context.getString(resId)

fun ViewHolder.getString(@StringRes resId: Int, vararg args: Any?) = context.getString(resId, *args)

fun <T : View> ViewHolder.findView(@IdRes resId: Int): T? = itemView.findViewById(resId)

fun <T : View> ViewHolder.lazyFindView(@IdRes resId: Int) = lazyNotSynchronized { findView<T>(resId) }