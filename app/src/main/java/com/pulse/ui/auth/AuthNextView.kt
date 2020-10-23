package com.pulse.ui.auth

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.pulse.R
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.use
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_auth_next.view.*

class AuthNextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView = inflate(R.layout.view_auth_next, true)

    private var text = ""

    init {
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.AuthNextView, defStyleAttr, -1)
                .use {
                    text = getString(R.styleable.AuthNextView_text) ?: ""
                }
        }

        setBackgroundResource(R.drawable.background_default_ripple_selector_rect)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        tvText.text = text
    }
}