package com.pulse.ui.auth

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.res.use
import com.pulse.R
import com.pulse.core.extensions.inflater
import com.pulse.databinding.ViewAuthNextBinding

class AuthNextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewAuthNextBinding.inflate(inflater, this)

    private var text = ""

    init {
        attrs?.let { attrSet ->
            context.theme.obtainStyledAttributes(attrSet, R.styleable.AuthNextView, defStyleAttr, -1)
                .use {
                    text = it.getString(R.styleable.AuthNextView_text) ?: ""
                }
        }

        setBackgroundResource(R.drawable.background_default_ripple_selector_rect)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding.mtvText.text = text
    }
}