package com.pulse.ui.auth

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.pulse.R
import com.pulse.core.extensions.inflater
import com.pulse.core.extensions.use
import com.pulse.databinding.ViewAuthNextBinding

class AuthNextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewAuthNextBinding.inflate(inflater, this)

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
        binding.mtvText.text = text
    }
}