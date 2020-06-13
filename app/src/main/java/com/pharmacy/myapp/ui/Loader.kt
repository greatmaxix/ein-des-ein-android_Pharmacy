package com.pharmacy.myapp.ui

import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import com.pharmacy.myapp.R

class Loader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {

    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            shader = SweepGradient(
                width / 2f,
                height / 2f,
                intArrayOf(Color.TRANSPARENT, context.getColor(R.color.colorLinkActive)),
                floatArrayOf(0.25f, 1f)
            )
            strokeWidth = 8f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, width / 2.2f, paint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animateRotate()
    }


    private fun animateRotate() {
        AnimatorInflater.loadAnimator(context, R.animator.animator_rotate).apply {
            setTarget(this)
            start()
        }
    }
}