package com.pharmacy.myapp.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.ActionMode
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.colorFrom
import com.pharmacy.myapp.core.extensions.textColor

class PinView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatEditText(context, attrs) {

    companion object {
        private const val NUM_CHARS = 4
    }

    private val radius by lazy { resources.getDimensionPixelSize(R.dimen._8sdp).toFloat() }
    private val strokeWidth by lazy { resources.getDimensionPixelSize(R.dimen._1sdp).toFloat() }

    private val circleWidth by lazy { radius + strokeWidth }

    private val spacing by lazy { (width - circleWidth * NUM_CHARS) / NUM_CHARS }

    private val paintCircle by lazy {
        Paint(paint).apply {
            style = Paint.Style.STROKE
            strokeWidth = this@PinView.strokeWidth
            isAntiAlias = true
        }
    }

    private var clickListener: OnClickListener? = null

    init {

        super.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu) = false
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = false
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem) = false
        })

        super.setOnClickListener {
            text?.length?.let(::setSelection)
            clickListener?.onClick(it)
        }

        super.setOnEditorActionListener { _, _, _ -> true }

        textColor(R.color.colorTransparent)
        setBackgroundResource(0)
        isCursorVisible = false
        setTextIsSelectable(false)
        imeOptions = EditorInfo.IME_ACTION_DONE
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(NUM_CHARS))
        keyListener = DigitsKeyListener.getInstance("0123456789")
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent) = true

    override fun setOnClickListener(l: OnClickListener?) {
        clickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback) =
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val cy = height / 2f
        for (position in 1 until 5) {
            var cx = circleWidth * position + (radius + spacing + strokeWidth * 2) * (position - 1)
            if (position == NUM_CHARS) cx -= strokeWidth
            canvas?.drawCircle(cx, cy, radius, paintCircle.updateColorForLines(position))
        }
    }

    private fun Paint.updateColorForLines(position: Int): Paint {
        if (length() == 0 && !isFocused) {
            style = Paint.Style.STROKE
            color = colorFrom(R.color.colorPinNormal)
        } else {
            style = Paint.Style.FILL
            color = colorFrom(if (isEnabled) position.pinColor else R.color.colorBackgroundInputTextPrimaryDisable)
        }
        return this
    }

    private val Int.pinColor
        get() = if (this <= length()) R.color.colorPinPrimary else R.color.colorPinNormal
}