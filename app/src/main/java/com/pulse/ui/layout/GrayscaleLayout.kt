package com.pulse.ui.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class GrayscaleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    var disabled = true
        set(value) {
            field = value
            requestLayout()
        }

    private val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        )
    }

    override fun dispatchDraw(canvas: Canvas?) {
        saveLayer(canvas)
        super.dispatchDraw(canvas)
        restore(canvas)
    }

    override fun draw(canvas: Canvas?) {
        saveLayer(canvas)
        super.draw(canvas)
        restore(canvas)
    }

    private fun saveLayer(canvas: Canvas?) {
        if (disabled) {
            canvas?.saveLayer(null, paint)
        }
    }

    private fun restore(canvas: Canvas?) {
        if (disabled) {
            canvas?.restore()
        }
    }
}