package com.pulse.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.pulse.core.extensions.getColor
import com.pulse.core.extensions.inputTextBackground
import com.pulse.core.extensions.inputTextColor

class SmsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs) {

    companion object {
        private const val NUM_CHARS = 5
    }

    private val density by lazy { resources.displayMetrics.density }

    private val paintText
        get() = Paint(paint).apply { color = getColor(isEnabled.inputTextColor) }
    private val paintLines
        get() = Paint(paint).apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            strokeWidth = density
            color = getColor(isEnabled.inputTextBackground)
        }

    private val space by lazy { 15f * density }
    private val spaceBetweenLines by lazy { 6f * density }
    private val cornerRadius = 16f

    private var clickListener: OnClickListener? = null

    // Sizes for drawing
    private val fullWidth by lazy { width - paddingRight - paddingLeft }
    private val spacingBottom by lazy { (height - paddingBottom).toFloat() }
    private val charSize by lazy { (fullWidth - space * (NUM_CHARS - 1)) / NUM_CHARS }

    init {
        super.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu) = false
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = false
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem) = false
        })
        super.setOnClickListener {
            setSelection(text?.length ?: 0)
            clickListener?.onClick(it)
        }

        setBackgroundResource(0)
        isCursorVisible = false
        setTextIsSelectable(false)
        imeOptions = EditorInfo.IME_ACTION_DONE
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(NUM_CHARS))
        keyListener = DigitsKeyListener.getInstance("0123456789")
    }

    override fun setOnClickListener(l: OnClickListener?) {
        clickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback) =
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")

    override fun onDraw(canvas: Canvas) {
        var startX = paddingLeft.toFloat()

        val textLength = text?.length ?: 0
        val textWidths = FloatArray(textLength)

        for (position in 0..NUM_CHARS) {

            canvas.drawRoundRect(getRect(startX, startX + charSize), cornerRadius, cornerRadius, paintLines)

            if (textLength > position) {
                val x = startX + charSize / 2 - textWidths.first() / 2
                val y = spacingBottom - spaceBetweenLines
                val text = text?.toString() ?: ""
                canvas.drawText(text, position, position + 1, x, y, paintText.apply { getTextWidths(text, 0, textLength, textWidths) })
            }

            startX += charSize + space
        }
    }

    private fun getRect(left: Float, right: Float) = RectF(left, 0f, right, height.toFloat())
}