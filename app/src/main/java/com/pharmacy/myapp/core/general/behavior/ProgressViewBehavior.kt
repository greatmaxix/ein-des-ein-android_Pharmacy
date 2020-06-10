package com.pharmacy.myapp.core.general.behavior

import android.view.View
import androidx.core.view.isGone
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.animateGone
import com.pharmacy.myapp.core.extensions.animateVisible
import com.pharmacy.myapp.core.view.LoaderView

class ProgressViewBehavior(private var progressRoot: View?) : IProgressBehavior {

    private val loaderView: LoaderView?
        get() = progressRoot?.findViewById(R.id.loaderView)

    override fun showProgress() {
        progressRoot?.apply {
            if (isGone) {
                animateVisible(50)
                loaderView?.start()
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