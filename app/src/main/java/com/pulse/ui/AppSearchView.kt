package com.pulse.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import com.pulse.R
import com.pulse.core.extensions.*
import com.pulse.databinding.LayoutSearchBinding
import com.pulse.ui.text.setTextWithCursorToEnd
import com.pulse.ui.text.setTextWithCursorToEndAndOpen
import kotlinx.android.extensions.LayoutContainer
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
) : CardView(context, attrs, defStyleAttr), LayoutContainer {

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

    override val containerView = inflate(R.layout.layout_search, true)

    var onBackClick: (() -> Unit)? = null

    init {
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.AppSearchView, defStyleAttr, -1)
                .use {
                    hint = getResourceId(R.styleable.AppSearchView_hintText, -1)
                    hintColor = getResourceId(R.styleable.AppSearchView_hintColor, R.color.colorGlobalBlack)
                    debounce = getFloat(R.styleable.AppSearchView_debounce, 200f)
                    animationDuration = getFloat(R.styleable.AppSearchView_debounce, 400f).toLong()
                    withBackButton = getBoolean(R.styleable.AppSearchView_withBackButton, false)
                }
        }
    }

    @FlowPreview
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

    override fun clearFocus() {
        super.clearFocus()
        binding.etSearch.clearFocus()
    }
}