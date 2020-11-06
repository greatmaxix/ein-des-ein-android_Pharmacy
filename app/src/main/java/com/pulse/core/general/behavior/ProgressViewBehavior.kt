package com.pulse.core.general.behavior

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.view.isGone
import com.pulse.R
import com.pulse.core.extensions.animateGone
import com.pulse.core.extensions.animateVisible

class ProgressViewBehavior(private var progressRoot: View?) : IProgressBehavior {

    private val anim: AnimatedVectorDrawable?
        get() = progressRoot?.findViewById<ImageView>(R.id.ivLoader)?.drawable?.run { this as AnimatedVectorDrawable }

    override fun showProgress() {
        progressRoot?.apply {
            if (isGone) {
                animateVisible(50)
                startAnimation()
            }
        }
    }

    override fun hideProgress() {
        progressRoot?.animateGone(50)
        anim?.clearAnimationCallbacks()
    }

    override fun detach() {
        progressRoot = null
    }

    private fun startAnimation() = anim?.apply {
        registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                start()
            }
        })
        start()
    }
}