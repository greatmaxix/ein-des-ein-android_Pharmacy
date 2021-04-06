package com.pulse.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.core.content.res.use
import androidx.core.view.isGone
import com.google.android.material.card.MaterialCardView
import com.pulse.R
import com.pulse.core.extensions.*
import com.pulse.databinding.LayoutSearchBinding
import com.pulse.ui.text.setTextWithCursorToEnd
import com.pulse.ui.text.setTextWithCursorToEndAndOpen
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.focusChanges
import reactivecircus.flowbinding.android.widget.editorActionEvents
import reactivecircus.flowbinding.android.widget.textChanges

class AppSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding = LayoutSearchBinding.inflate(inflater, this, true)
    private var hint = -1
    private var hintColor = -1
    private var debounce = 500f
    private var notifyJob: Job? = null
    private var animationDuration = 400L
    private var withBackButton = false
        set(value) {
            field = value
            binding.ivBack.visibleOrGone(value)
        }
    private var notify: ((CharSequence) -> Unit)? = null
    private var editor: ((String) -> Boolean)? = null
    private val viewJob = SupervisorJob()
    private val viewScope = CoroutineScope(Main.immediate + viewJob)
    private val whiteColor = getColor(R.color.colorGlobalWhite)
    private val semiWhiteColor = getColor(R.color.searchBackgroundBlue)
    private val darkBluerColor = getColor(R.color.darkBlue)
    private var hasFocus: Boolean = false
        set(value) {
            field = value
            with(binding) {
                post {
                    if (value) {
                        mtvHint.setTextColor(darkBluerColor)
                        setCardBackgroundColor(whiteColor)
                        cardElevation = 1f
                        mtvHint.compoundDrawables.getOrNull(0)?.setTint(darkBluerColor)
                    } else {
                        mtvHint.setTextColor(whiteColor)
                        setCardBackgroundColor(semiWhiteColor)
                        cardElevation = 0f
                        mtvHint.compoundDrawables.getOrNull(0)?.setTint(whiteColor)
                    }
                }
            }
        }
    var onBackClick: (() -> Unit)? = null

    init {
        attrs?.let { attrSet ->
            context.theme.obtainStyledAttributes(attrSet, R.styleable.AppSearchView, defStyleAttr, -1)
                .use {
                    hint = it.getResourceId(R.styleable.AppSearchView_hintText, -1)
                    hintColor = it.getResourceId(R.styleable.AppSearchView_hintColor, R.color.colorGlobalWhite)
                    debounce = it.getFloat(R.styleable.AppSearchView_debounce, 200f)
                    animationDuration = it.getFloat(R.styleable.AppSearchView_debounce, 400f).toLong()
                    withBackButton = it.getBoolean(R.styleable.AppSearchView_withBackButton, false)
                }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onFinishInflate() = with(binding) {
        super.onFinishInflate()

        if (hint != -1) mtvHint.setText(hint)
        if (hintColor != -1) mtvHint.setTextColorRes(hintColor)

        etSearch.textChanges()
            .skipInitialValue()
            .onEach {
                notifySearchListener(it)
                val isContainsText = it.isNotEmpty()
                mtvHint.isGone = isContainsText
                if (isContainsText) ivClose.animateVisibleIfNot(animationDuration) else ivClose.animateGoneIfNot(animationDuration)
                if (!ivClose.hasOnClickListeners()) {
                    ivClose.setOnClickListener(closeClick)
                }
            }
            .launchIn(viewScope)

        etSearch.focusChanges()
            .skipInitialValue()
            .onEach {
                hasFocus = it
                if (it) {
                    ivClose.setOnClickListener(closeClick)
                }
            }
            .launchIn(viewScope)

        etSearch
            .editorActionEvents { hideKeyboard(editor?.invoke(etSearch.text()) ?: false) }
            .launchIn(viewScope)

        ivBack.setDebounceOnClickListener {
            onBackClick?.invoke()
        }
        clearFocus()
    }

    private val closeClick = OnClickListener {
        binding.ivClose.setOnClickListener(null)
        binding.ivClose.animateGoneIfNot(animationDuration)
        binding.etSearch.clearText()
    }

    override fun onDetachedFromWindow() {
        viewJob.cancel()
        super.onDetachedFromWindow()
    }

    private suspend fun notifySearchListener(text: CharSequence) = notify?.let {
        if (notifyJob?.isActive == true) {
            notifyJob?.cancel()
        }

        notifyJob = viewScope.launch {
            it(withContext(Default) {
                delay(debounce.toLong())
                text
            })
        }
    }

    val text
        get() = binding.etSearch.text()

    fun setSearchListener(action: (CharSequence) -> Unit) {
        notify = action
    }

    fun setEditorListener(action: (String) -> Boolean) {
        editor = action
    }

    fun setText(value: String) {
        binding.etSearch.setTextWithCursorToEnd(value)
        binding.ivClose.setOnClickListener(closeClick)
    }

    fun setTextAndOpen(value: String) {
        if (text != value) {
            binding.etSearch.setTextWithCursorToEndAndOpen(value)
            binding.ivClose.setOnClickListener(closeClick)
        }
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?) = binding.etSearch.requestFocus(direction, previouslyFocusedRect)
        .also { hasFocus = true }

    override fun clearFocus() {
        super.clearFocus()
        binding.etSearch.clearFocus()
        hasFocus = false
    }
}