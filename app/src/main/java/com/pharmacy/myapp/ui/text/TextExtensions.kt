package com.pharmacy.myapp.ui.text

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.text.*
import android.text.InputType.TYPE_CLASS_NUMBER
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.textfield.TextInputLayout
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.*
import java.math.BigDecimal

private const val MIN_PHONE_LENGTH = 11
private const val MAX_PHONE_LENGTH = 16

fun EditText.generalRule() {
    inputType = InputType.TYPE_CLASS_TEXT
}

fun EditText.phoneRule(prefix: (() -> String)? = null) {
    inputType = TYPE_CLASS_NUMBER
    filters = arrayOf(
        InputFilter.LengthFilter(MAX_PHONE_LENGTH),
        InputFilter { source, start, end, dest, dstart, dend ->
            if (!generateResult(source, start, end, dest, dstart, dend).startsWith(prefix?.invoke() ?: "")) setTextWithCursorToEnd("${text()}$source") else null
        })
}

fun EditText.generateResult(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int) =
    StringBuilder(dest).apply { replace(dstart, dend, source.subSequence(start, end).toString()) }

inline fun EditText.setSingleClick(crossinline click: (View) -> Unit) {
    val detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            click.invoke(this@setSingleClick)
            return super.onSingleTapUp(e)
        }
    })
    inputType = EditorInfo.TYPE_NULL
    keyListener = null
    isClickable = false
    isFocusable = false
    isCursorVisible = false
    setOnTouchListener { _, event -> detector.onTouchEvent(event) }
}

fun EditText.numberRule() {
    inputType = TYPE_CLASS_NUMBER
}

fun EditText.substring(startIndex: Int) = when {
    isEmpty() -> ""
    else -> {
        val substring = text().substring(startIndex)
        if (substring.isEmpty()) "" else substring
    }
}

fun EditText.cursorToEnd() {
    val textLength = text().length
    if (textLength > 0) {
        setSelection(textLength)
    }
}

fun EditText.setTextWithCursorToEnd(text: String): String {
    setText(text)
    cursorToEnd()
    return ""
}

/* TextInputLayout */

fun TextInputLayout.cursorToEnd(): TextInputLayout {
    editText?.cursorToEnd()
    return this
}

fun TextInputLayout.substring(startIndex: Int) = editText?.substring(startIndex) ?: ""

fun TextInputLayout.setPhoneRule(prefix: (() -> String)? = null): TextInputLayout {
    editText?.phoneRule(prefix)
    return this
}

fun TextInputLayout.isEmptyWithError(error: String? = null): Boolean {
    val isNullOrBlank = text().isBlank()
    if (isNullOrBlank) {
        showError(error)
    }
    return isNullOrBlank
}

fun TextInputLayout.showError(message: String?): Boolean {
    message?.let {
        isErrorEnabled = true
        error = it
    }
    return false
}

fun TextInputLayout.hideError(): Boolean {
    if (isErrorEnabled) {
        isErrorEnabled = false
        error = null
    }
    return true
}

fun TextInputLayout.errorAction(shake: Boolean = true, action: (() -> Unit)? = null): Boolean {
    action?.invoke()
    if (shake) {
        animateShake()
    }
    return false
}

fun TextInputLayout.checkGeneral(message: String? = null) = isEmptyWithError(message).also {
    if (it) {
        animateShake()
    }
}

fun TextInputLayout.isPhoneNumberValid(message: String? = null) = when {
    isEmptyWithError(message) -> errorAction()
    editText?.text()?.length ?: 0 < MIN_PHONE_LENGTH -> errorAction { showError(context.getString(R.string.MinLengthIsCharacters)) }
    else -> {
        this.error = null
        true
    }
}

fun TextInputLayout.checkAmount(message: String? = null) =
    if (isEmptyWithError(message)) errorAction() else true

fun TextInputLayout.checkLength(message: String? = null): Boolean {
    val matches = editText?.text?.matches(Regex("^[a-zA-Z0-9]{2,12}$")).falseIfNull() // \u0400-\u04FF for cyrillic
    if (!matches) showError(message)
    return matches
}


fun TextInputLayout.checkEmail(error: String? = null): Boolean {
    val isEmailValid =
        editText?.text?.matches(Regex("^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$"))
            .falseIfNull()
    if (!isEmailValid) showError(error)
    return isEmailValid
}

fun TextInputLayout.setText(text: String?) {
    editText?.let {
        it.setText(text)
        if (text != null && it.hasFocus()) {
            it.setSelection(it.length())
        }
    }
}

fun TextInputLayout.setText(text: BigDecimal?) {
    setText(text?.toString())
}

fun TextInputLayout.requestFocusWithSelection(): Int {
    editText?.requestFocus()
    val textLength = text().length
    if (textLength > 0) {
        editText?.setSelection(textLength)
    }
    return id
}

fun TextInputLayout.onDoneImeAction(clearFocus: Boolean = false, action: () -> Unit): TextInputLayout {
    editText?.onDoneImeAction(clearFocus, action)
    return this
}

fun TextInputLayout.animateEnableOrDisable(enable: Boolean) {
    AnimatorSet().apply {
        playTogether(
            ValueAnimator.ofObject(ArgbEvaluator(), editText?.currentTextColor, colorCompat(enable.inputTextColor)).apply {
                addUpdateListener {
                    val color = it.animatedValue as Int
                    editText?.setTextColor(color)
                    endIconDrawable?.apply { mutate().setTint(color) }
                }
            },
            ValueAnimator.ofObject(ArgbEvaluator(), boxBackgroundColor, colorCompat(enable.inputTextBackground)).apply {
                addUpdateListener { boxBackgroundColor = it.animatedValue as Int }
            })
        duration = resources.getInteger(R.integer.animation_time).toLong()
        interpolator = FastOutSlowInInterpolator()
        start()
    }
    enableOrDisable(enable)
}

fun EditText.addAfterTextWatcher(doAfter: (String)-> Unit): TextWatcher {
    val value = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            doAfter(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
    addTextChangedListener(value)
    return value
}