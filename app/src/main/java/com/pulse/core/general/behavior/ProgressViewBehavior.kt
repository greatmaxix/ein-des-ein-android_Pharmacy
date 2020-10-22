package com.pulse.core.general.behavior

import android.view.View
import androidx.core.view.isGone
import com.pulse.core.extensions.animateGone
import com.pulse.core.extensions.animateVisible

class ProgressViewBehavior(private var progressRoot: View?) : IProgressBehavior {

    override fun showProgress() {
        progressRoot?.apply {
            if (isGone) {
                animateVisible(50)
            }
        }
    }

    override fun hideProgress() {
        progressRoot?.animateGone(50)
    }

    override fun detach() {
        progressRoot = null
    }
}