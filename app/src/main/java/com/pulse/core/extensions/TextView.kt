package com.pulse.core.extensions

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import com.pulse.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun TextView.hideKeyboardOnEditorAction() {
    setOnEditorActionListener { _, _, _ ->
        hideKeyboard()
        true
    }
}

fun TextView.text(): String = text.toString()

fun TextView.underlineSpan(): TextView {
    text = text().underlineSpan()
    return this
}

fun TextView.setTextColorRes(@ColorRes colorResId: Int): TextView {
    setTextColor(getColor(colorResId))
    return this
}

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

fun TextView.animateTextColor(to: Int, duration: Long = resources.getInteger(R.integer.animation_time).toLong()): TextView {
    valueAnimatorARGB(currentTextColor, to, duration) { setTextColor(it) }
    return this
}

fun TextView.animateEnableOrDisable(enable: Boolean, to: Int, duration: Long = resources.getInteger(R.integer.animation_time).toLong()) {
    enableOrDisable(enable)
    animateTextColor(to, duration)
}

fun TextView.clearText() {
    text = ""
}

fun TextView.setTextHtml(text: String?) = setText(text?.wrapHtml, TextView.BufferType.SPANNABLE)

fun AppCompatTextView.setTextAsync(text: String?) {
    if (!text.isNullOrEmpty()) {
        setTextFuture(PrecomputedTextCompat.getTextFuture(text, TextViewCompat.getTextMetricsParams(this), null))
    }
}

suspend fun TextView.setPrecomputedTextCompat(text: String) =
    TextViewCompat.setPrecomputedText(this, withContext(Dispatchers.Default) { createPrecomputedTextCompat(text) })

fun TextView.createPrecomputedTextCompat(text: String): PrecomputedTextCompat =
    PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))