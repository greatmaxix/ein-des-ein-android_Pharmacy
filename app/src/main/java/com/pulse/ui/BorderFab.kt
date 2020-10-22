package com.pulse.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pulse.R

class BorderFab @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr) {

    private val strokePaint by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = resources.getDimensionPixelSize(R.dimen._2sdp).toFloat()
            isAntiAlias = true
            color = Color.WHITE
            z = 1f
        }
    }
    private var borderEnabled = false

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (measuredWidth == 0) return
        if (borderEnabled) canvas.drawCircle(measuredWidth.toFloat() / 2f, measuredWidth.toFloat() / 2f, measuredWidth / 2.3f, strokePaint)
    }

    fun setBorderEnabled(enable: Boolean) {
        borderEnabled = enable
        invalidate()
    }
}