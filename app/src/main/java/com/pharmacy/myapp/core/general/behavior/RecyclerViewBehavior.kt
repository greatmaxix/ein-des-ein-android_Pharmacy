package com.pharmacy.myapp.core.general.behavior

import android.animation.TimeInterpolator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.myapp.core.extensions.visible

class RecyclerViewBehavior(private var recyclerView: RecyclerView?, private var view: View?) : IRecyclerBehavior {

    companion object {
        private const val ACCURACY_COEFFICIENT = 25f
    }

    private var scrollDist = 0
    private var isVisible = false
        set(value) {
            field = value
            scrollDist = 0
        }

    init {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when {
                    isNeedToHide -> hide()
                    isNeedToShow -> show()
                }
                if (isVisible && dy > 0 || !isVisible && dy < 0) {
                    scrollDist += dy
                }
            }
        })
    }

    override fun show() {
        view?.visible()
        isVisible = view?.move(0f, DecelerateInterpolator(2f)) ?: false
    }

    override fun hide() {
        isVisible = view?.move() ?: false
    }

    override fun detach() {
        view = null
        recyclerView = null
    }

    private fun View.move(valueY: Float = height.toFloat(), interpolator: TimeInterpolator = AccelerateInterpolator(2f)): Boolean {
        animate().translationY(valueY).setInterpolator(interpolator).start()
        return valueY <= 0f
    }

    private val isNeedToHide
        get() = isVisible && scrollDist > ACCURACY_COEFFICIENT

    private val isNeedToShow
        get() = !isVisible && scrollDist < -ACCURACY_COEFFICIENT

}