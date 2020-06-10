package com.pharmacy.myapp.core.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.inflate

@Suppress("LeakingThis")
abstract class LoaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var started = false

    protected var tintColor = Color.TRANSPARENT
    private var instantStart = false

    init {
        val layoutId = getLayoutLoaderId()
        if (layoutId != 0 ) {
            inflate(layoutId, true)
        }
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.LoaderView, defStyleAttr, defStyleRes)
            try {
                tintColor = a.getColor(R.styleable.LoaderView_android_tint, tintColor)
                instantStart = a.getBoolean(R.styleable.LoaderView_instantStart, instantStart)
            } finally {
                a.recycle()
            }
        }
    }

    @LayoutRes
    protected abstract fun getLayoutLoaderId(): Int

    override fun onFinishInflate() {
        super.onFinishInflate()
        configViews()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (instantStart) {
            start()
        }
    }

    protected abstract fun configViews()

    fun start() {
        if (!started) {
            started = true
            performStart()
        }
    }

    protected abstract fun performStart()

    fun stop() {
        if (started) {
            started = false
            performStop()
        }
    }

    protected abstract fun performStop()

}