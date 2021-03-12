package com.pulse.ui.auth

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import com.pulse.R
import com.pulse.core.extensions.inflater
import com.pulse.core.extensions.onClickDebounce
import com.pulse.databinding.ViewAuthFooterBinding

class AuthFooter @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewAuthFooterBinding.inflate(inflater, this)

    private var textAction = ""
    private var textDescription = ""

    init {
        attrs?.let { attrSet ->
            context.theme.obtainStyledAttributes(attrSet, R.styleable.AuthFooter, defStyleAttr, -1)
                .use {
                    textAction = it.getString(R.styleable.AuthFooter_textAction) ?: ""
                    textDescription = it.getString(R.styleable.AuthFooter_textDescription) ?: ""
                }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding.mtvAction.text = textAction
        binding.mtvDescription.text = textDescription
    }

    fun setOnSkipClickListener(action: () -> Unit): AuthFooter {
        binding.mtvSkip.onClickDebounce(action)
        return this
    }

    fun setOnActionClickListener(action: () -> Unit): AuthFooter {
        binding.mtvAction.onClickDebounce(action)
        return this
    }
}