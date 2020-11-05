package com.pulse.core.general.behavior

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.view.isGone
import com.pulse.R
import com.pulse.core.extensions.animateGone
import com.pulse.core.extensions.animateVisible

class ProgressViewBehavior(private var progressRoot: View?) : IProgressBehavior {

    private val loader: ImageView?
        get() = progressRoot?.findViewById(R.id.ivLoader)

    override fun showProgress() {
        progressRoot?.apply {
            if (isGone) {
                animateVisible(50)
            }
        }
        loader?.apply { (drawable as AnimatedVectorDrawable).start() }
    }

    override fun hideProgress() {
        progressRoot?.animateGone(50)
    }

    override fun detach() {
        progressRoot = null
    }
}