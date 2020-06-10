package com.pharmacy.myapp.core.base.adapter

import android.content.res.Resources.NotFoundException
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.myapp.core.extensions.compatColor
import com.pharmacy.myapp.core.extensions.dimensionPixelSize
import com.pharmacy.myapp.core.extensions.drawable
import com.pharmacy.myapp.core.extensions.stringRes
import kotlinx.android.extensions.LayoutContainer
import timber.log.Timber

abstract class BaseViewHolder<T>(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    abstract fun bind(item: T)

    protected val Int.toText get() = stringRes(this)

    protected val Int.toDrawable
        get() = try {
            drawable(this)
        } catch (e: NotFoundException) {
            Timber.e(e)
            null
        }

    protected val Int.toColor get() = compatColor(this)

    protected val Int.toPixelSize get() = dimensionPixelSize(this)
}