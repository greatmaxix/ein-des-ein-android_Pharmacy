package com.pulse.core.extensions

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import com.pulse.R

fun TextView.onDoneImeAction(clearFocus: Boolean = false, action: () -> Unit) {
    setOnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            action.invoke()
            if (clearFocus) v.clearFocus()
        }
        false
    }
}

fun TextView.onDoneImeAction(action: () -> Unit): TextView {
    onDoneImeAction(false, action)
    return this
}

fun TextView.animateTextColor(
    to: Int,
    duration: Long = resources.getInteger(R.integer.animation_time).toLong()
): TextView {
    colorValueAnimator(currentTextColor, to, duration) { setTextColor(it) }
    return this
}

fun TextView.animateEnableOrDisable(
    enable: Boolean,
    to: Int,
    duration: Long = resources.getInteger(R.integer.animation_time).toLong()
) {
    enableOrDisable(enable)
    animateTextColor(to, duration)
}

fun AppCompatTextView.setTextAsync(text: String?) {
    if (!text.isNullOrEmpty()) {
        setTextFuture(PrecomputedTextCompat.getTextFuture(text, TextViewCompat.getTextMetricsParams(this), null))
    }
}

fun TextView.clearText() {
    text = ""
}

fun TextView.setTextHtml(text: String?) = setText(text?.wrapHtml, TextView.BufferType.SPANNABLE)