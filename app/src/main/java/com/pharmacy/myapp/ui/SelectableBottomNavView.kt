package com.pharmacy.myapp.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.navigation.NavDestination
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.getCompatColor
import com.pharmacy.myapp.core.extensions.getCompatDrawable
import com.pharmacy.myapp.core.extensions.inflate

class SelectableBottomNavView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    private val selectedBorder = resources.getDimensionPixelSize(R.dimen._2sdp).toFloat()
    private val selectedColor = context.getCompatColor(R.color.primaryBlue)
    private val defaultColor = context.getCompatColor(R.color.grey)
    private var lastSelectedItem: NavItem? = null
    private var fabView: BorderFab? = null
    var navItems: List<NavItem> = listOf()
        set(value) {
            field = value
            if (fabView != null) {
                removeView(fabView)
                fabView = null
            }
            updateProfileIconState()
            val hasFab = value.firstOrNull { it.isFab } != null
            if (hasFab) {
                fabView = inflate(R.layout.view_search_fab, false) as BorderFab
                addView(fabView)
            }
            value.forEach {
                if (!it.isFab) setItemColor(it, defaultColor) else fabView?.setBorderEnabled(false)
            }
        }

    private fun setItemColor(it: NavItem, @ColorInt color: Int) {
        if (it.iconResId != null) {
            menu.findItem(it.menuItemId)?.icon = context.getCompatDrawable(it.iconResId)?.apply {
                DrawableCompat.setTint(this, color)
            }
        }
    }

    private fun updateProfileIconState(selected: Boolean = false) {
        val item = navItems.firstOrNull { it.iconUrl != null }
        if (item != null) {
            if (item.iconUrl.isNullOrEmpty().not()) {
                Glide.with(context)
                    .asBitmap()
                    .load(item.iconUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(object : CustomTarget<Bitmap>(AVATAR_SIZE, AVATAR_SIZE) {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            menu.findItem(item.menuItemId)?.icon = resource.run {
                                RoundedBitmapDrawableFactory.create(
                                    resources,
                                    if (selected) createBitmapWithBorder(selectedBorder, selectedColor) else this
                                ).apply {
                                    isCircular = true
                                }
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // no op
                        }
                    })
            } else {
                menu.findItem(item.menuItemId)?.icon = context.getCompatDrawable(R.drawable.ic_avatar)
            }
        }
    }

    fun changeSelection(destination: NavDestination) {
        if (navItems.isEmpty()) throw Throwable("You must set navItems first")

        val currentNavItem = navItems.find { it.navigationItemResId == destination.id }
        lastSelectedItem?.let {
            when {
                it.iconUrl != null -> updateProfileIconState(false)
                it.isFab -> fabView?.setBorderEnabled(false)
                else -> setItemColor(it, defaultColor)
            }
        }
        currentNavItem?.let {
            when {
                it.iconUrl != null -> updateProfileIconState(true)
                it.isFab -> fabView?.setBorderEnabled(true)
                else -> setItemColor(it, selectedColor)
            }
        }

        lastSelectedItem = currentNavItem
    }

    private fun Bitmap.createBitmapWithBorder(borderSize: Float, borderColor: Int): Bitmap {
        val borderOffset = (borderSize * 2).toInt()
        val halfWidth = width / 2
        val halfHeight = height / 2
        val circleRadius = halfWidth.coerceAtMost(halfHeight).toFloat()
        val newBitmap = Bitmap.createBitmap(
            width + borderOffset,
            height + borderOffset,
            Bitmap.Config.ARGB_8888
        )

        val centerX = halfWidth + borderSize
        val centerY = halfHeight + borderSize

        val paint = Paint()
        val canvas = Canvas(newBitmap).apply {
            drawARGB(0, 0, 0, 0)
        }

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, circleRadius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, borderSize, borderSize, paint)

        paint.xfermode = null
        paint.style = Paint.Style.STROKE
        paint.color = borderColor
        paint.strokeWidth = borderSize
        canvas.drawCircle(centerX, centerY, circleRadius, paint)
        return newBitmap
    }

    data class NavItem(
        @IdRes val menuItemId: Int,
        @IdRes val navigationItemResId: Int,
        @DrawableRes val iconResId: Int?,
        val iconUrl: String?,
        val isFab: Boolean = false
    )

    companion object {

        private const val AVATAR_SIZE = 128
    }
}