package com.pharmacy.myapp.utils.extension

import android.animation.AnimatorInflater
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.pharmacy.myapp.R

fun View.animateRotate() {
    AnimatorInflater.loadAnimator(context, R.animator.animator_rotate).apply {
        setTarget(this@animateRotate)
        start()
    }
}

fun View.colorValueAnimator(from: Int, to: Int, duration: Long, onUpdate: (Int) -> Unit): View {
    ValueAnimator.ofObject(ArgbEvaluator(), from, to).apply {
        addUpdateListener { onUpdate(it.animatedValue as Int) }
        this.duration = duration
        start()
    }
    return this
}

fun View.animateLogoTopMargin(enable: Boolean): View {
    val newMargin = resources.getDimensionPixelSize(enable.logoTopMargin)
    val lp = layoutParams as ConstraintLayout.LayoutParams
    ValueAnimator.ofInt(lp.topMargin, newMargin).apply {
        duration = resources.getInteger(R.integer.animation_time).toLong()
        interpolator = FastOutSlowInInterpolator()
        addUpdateListener { updateLayoutParams<ConstraintLayout.LayoutParams> { topMargin = it.animatedValue as Int } }
        start()
    }
    return this
}