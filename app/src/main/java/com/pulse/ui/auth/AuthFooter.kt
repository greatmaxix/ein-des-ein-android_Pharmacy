package com.pulse.ui.auth

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.pulse.R
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.onClickDebounce
import com.pulse.core.extensions.use
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_auth_footer.view.*

class AuthFooter @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView = inflate(R.layout.view_auth_footer, true)

    private var textAction = ""
    private var textDescription = ""

    init {
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.AuthFooter, defStyleAttr, -1)
                .use {
                    textAction = getString(R.styleable.AuthFooter_textAction) ?: ""
                    textDescription = getString(R.styleable.AuthFooter_textDescription) ?: ""
                }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        tvAction.text = textAction
        tvDescription.text = textDescription
    }

    fun setOnSkipClickListener(action: () -> Unit): AuthFooter {
        mtvSkip.onClickDebounce(action)
        return this
    }

    fun setOnActionClickListener(action: () -> Unit): AuthFooter {
        tvAction.onClickDebounce(action)
        return this
    }
}