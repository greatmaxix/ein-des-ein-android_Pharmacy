package com.pulse.core.base.adapter

import android.content.res.Resources.NotFoundException
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pulse.core.extensions.color
import com.pulse.core.extensions.dimensionPixelSize
import com.pulse.core.extensions.drawable
import com.pulse.core.extensions.stringRes
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

    protected val Int.toColor get() = color(this)

    protected val Int.toPixelSize get() = dimensionPixelSize(this)
}