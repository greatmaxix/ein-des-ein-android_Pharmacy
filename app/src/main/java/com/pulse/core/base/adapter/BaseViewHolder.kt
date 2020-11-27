package com.pulse.core.base.adapter

import android.content.res.Resources.NotFoundException
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pulse.core.extensions.getColor
import com.pulse.core.extensions.getDimensionPixelSize
import com.pulse.core.extensions.getDrawable
import com.pulse.core.extensions.getString
import kotlinx.android.extensions.LayoutContainer
import timber.log.Timber

abstract class BaseViewHolder<T>(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    abstract fun bind(item: T)

    protected val Int.toText get() = getString(this)

    protected val Int.toDrawable
        get() = try {
            getDrawable(this)
        } catch (e: NotFoundException) {
            Timber.e(e)
            null
        }

    protected val Int.toColor get() = getColor(this)

    protected val Int.toPixelSize get() = getDimensionPixelSize(this)
}