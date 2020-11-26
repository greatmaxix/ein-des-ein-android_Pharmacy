package com.pulse.core.extensions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Paint
import android.graphics.Rect
import android.os.SystemClock
import android.view.View
import android.view.View.*
import android.view.ViewParent
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.FontRes
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isGone
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.core.widget.TextViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.Shapeable
import com.google.android.material.textfield.TextInputLayout
import com.pulse.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import timber.log.Timber
import kotlin.collections.mutableMapOf
import kotlin.collections.set

inline fun <T, reified R : View> R.singleAsyncTask(
    crossinline task: suspend () -> T?,
    crossinline result: R.(T?) -> Unit
) {

    val job = CoroutineScope(Default)

    val attachListener = object : OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(p0: View?) {}
        override fun onViewDetachedFromWindow(p0: View?) {
            job.cancel()
        }
    }

    addOnAttachStateChangeListener(attachListener)

    job.launch {
        val data = try {
            task()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        if (isActive) {
            try {
                withContext(Main) { result(data) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        removeOnAttachStateChangeListener(attachListener)
    }
}

inline fun <reified T : ViewParent> View.castParent(block: T.() -> Unit) {
    if (parent is T) {
        block(parent as T)
    } else {
        throw Exception("Wrong cast parent: ${parent.javaClass.simpleName} is not a ${T::class.java.simpleName}")
    }
}

fun View.adjustSizePreDraw() {
    doOnPreDraw {
        layoutParams = layoutParams.also {
            it.height = height
            it.width = width
        }
    }
}

fun View.animateShake() {
    ObjectAnimator.ofFloat(this, "translationX", 0f, 25f, -25f, 0f).apply {
        duration = 30
        repeatCount = 5
        repeatMode = ValueAnimator.REVERSE
        start()
    }
}

fun View.setWidth(width: Int) = updateLayoutParams { this.width = width }

fun View.setHeight(height: Int) = updateLayoutParams { this.height = height }

fun View.scaleWidth(to: Int, duration: Long = resources.getInteger(R.integer.animation_time).toLong()) =
    intValueAnimator(width, to, duration, ::setWidth)

fun View.scaleHeight(to: Int, duration: Long = resources.getInteger(R.integer.animation_time).toLong()) =
    intValueAnimator(height, to, duration, ::setHeight)

fun View.intValueAnimator(from: Int, to: Int, duration: Long, onUpdate: (Int) -> Unit): View {
    ValueAnimator.ofInt(from, to).apply {
        addUpdateListener { onUpdate(it.animatedValue as Int) }
        interpolator = FastOutSlowInInterpolator()
        this.duration = duration
        start()
    }
    return this
}

fun View.gone() {
    if (visibility != GONE) {
        isGone = true
    }
}

fun View.animateGone(duration: Long = 100) {
    animate().setDuration(duration).alpha(0f).withEndAction {
        visibility = GONE
    }
}

fun View.animateGoneIfNot(duration: Long = 100) {
    if (visibility != GONE) {
        animateGone(duration)
    }
}

fun View.visible() {
    if (visibility != VISIBLE) {
        visibility = VISIBLE
    }
}

fun View.animateVisible(duration: Long = 100) {
    alpha = 0f
    visible()
    animate().setDuration(duration).alpha(1f).withEndAction { alpha = 1f }
}

fun View.animateVisibleIfNot(duration: Long = 100) {
    if (visibility != VISIBLE) {
        animateVisible(duration)
    }
}

fun View.invisible() {
    if (visibility != INVISIBLE) {
        visibility = INVISIBLE
    }
}

fun View.animateInvisible(duration: Long = 100) {
    animate().setDuration(duration).alpha(0f).withEndAction {
        visibility = INVISIBLE
    }
}

fun View.animateInvisibleIfNot(duration: Long = 100) {
    if (visibility != INVISIBLE) {
        animateInvisible(duration)
    }
}

fun View.animateVisibleOrGone(duration: Long = 100) {
    if (visibility == VISIBLE) {
        animateGone(duration)
    } else {
        animateVisible(duration)
    }
}

fun View.alphaVisibleOrGone(duration: Long = 100) {
    Timber.e("Alpha: $alpha")
    if (alpha <= 0f) {
        animateVisible(duration)
    } else {
        animateGone(duration)
    }
}

fun View.animateVisibleOrGone(visible: Boolean, duration: Long = 100) {
    if (visible) animateVisible(duration) else animateGone(duration)
}

fun View.visibleOrGone(visible: Boolean) {
    if (visible) visible() else gone()
}

fun View.animateVisibleOrGoneIfNot(visible: Boolean, duration: Long = 100) {
    if (visible) animateVisibleIfNot(duration) else animateGoneIfNot(duration)
}

fun View.visibleOrInvisible(visible: Boolean) {
    visibility = if (visible) VISIBLE else INVISIBLE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.enableOrDisable() {
    isEnabled = !isEnabled
}

fun View.enableOrDisable(enable: Boolean) {
    isEnabled = enable
}

val View.inputMethodManager
    get() = context.inputMethodManager

fun View.hideKeyboard(needClearFocus: Boolean = true) =
    inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
        .also { if (needClearFocus) clearFocus() } ?: false

val View.isKeyboardOpen
    get() = Rect().run {
        val screenHeight = rootView.height
        getWindowVisibleDisplayFrame(this)
        screenHeight - bottom > screenHeight * 0.15
    }

val View.isKeyboardNotOpen
    get() = !isKeyboardOpen

fun View.translationYAnim(value: Float, duration: Long = 250, action: ((animator: Animator) -> Unit)? = null) {
    ObjectAnimator.ofFloat(this, "translationY", value)
        .apply {
            this.duration = duration
            action?.let { doOnEnd(it) }
            start()
        }
}

fun View.toggle(needAnim: Boolean = false, duration: Long = 250, action: ((animator: Animator) -> Unit)? = null) {
    if (needAnim) {
        translationYAnim(if (translationY > 0) 0f else height.toFloat(), duration, action)
    } else {
        translationY = if (translationY > 0) 0f else height.toFloat()
    }
}

fun View.rotate(angle: Float, endAction: () -> Unit, duration: Long = 300) = animate()
    .rotation(angle)
    .withEndAction { endAction.invoke() }
    .setDuration(duration)
    .start()

fun View.getColor(@ColorRes colorRes: Int) = context.getColor(colorRes)

fun View.getColorStateList(@ColorRes colorRes: Int) = context.getColorStateList(colorRes)

fun View.setDebounceOnClickListener(interval: Long = 400, listener: View.() -> Unit) {
    val lastClickMap = mutableMapOf<Int, Long>()
    setOnClickListener { v ->
        val currentTimestamp = SystemClock.uptimeMillis()
        if (currentTimestamp - lastClickMap.getOrDefault(v.id, 0) > interval) run { listener(v) }
        lastClickMap[v.id] = currentTimestamp
    }
}

inline fun View.onClickDebounce(crossinline f: () -> Unit) = setDebounceOnClickListener { f() }

fun View.setTopRoundCornerBackground(
    radius: Float = resources.getDimension(R.dimen._8sdp),
    elevation: Float = resources.getDimension(R.dimen._4sdp),
    @ColorInt tintColor: Int = ContextCompat.getColor(context, R.color.colorGlobalWhite)
) {
    val appearanceModel = ShapeAppearanceModel()
        .toBuilder()
        .setTopRightCorner(CornerFamily.ROUNDED, radius)
        .setTopLeftCorner(CornerFamily.ROUNDED, radius)
        .build()

    val shape = MaterialShapeDrawable(appearanceModel)
        .apply {
            shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
            this.elevation = elevation
            setTint(tintColor)
            paintStyle = Paint.Style.FILL
        }

    ViewCompat.setBackground(this, shape)
}

fun Shapeable.setBottomRoundCornerBackground(radius: Float) {
    val appearanceModel = ShapeAppearanceModel()
        .toBuilder()
        .setBottomRightCorner(CornerFamily.ROUNDED, radius)
        .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
        .build()

    val shape = MaterialShapeDrawable(appearanceModel).apply {
        paintStyle = Paint.Style.FILL
    }
    shapeAppearanceModel = shape.shapeAppearanceModel
}

fun View.mockToast(text: String = context.getString(R.string.expectSoonMock)) = this.setDebounceOnClickListener {
    context.toast(text)
}

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

suspend fun TextView.setPrecomputedTextCompat(text: String) =
    TextViewCompat.setPrecomputedText(this, withContext(Default) { createPrecomputedTextCompat(text) })

fun TextView.createPrecomputedTextCompat(text: String): PrecomputedTextCompat =
    PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))

/*fun TextInputLayout.setText(text: String?) {
    editText?.run {
        setText(text)
    }
}*/

fun TextInputLayout.text() = editText?.text() ?: ""

fun EditText.isEmpty() = text().isEmpty()

fun NestedScrollView.scrollToBottom() {
    scrollBy(0, lastChild.bottom + paddingBottom - (scrollY + height))
}

fun NestedScrollView.scrollToTop() {
    scrollTo(0, 0)
}

fun View.getDimension(@DimenRes dimenResId: Int) = resources.getDimension(dimenResId)
fun View.lazyDimension(@DimenRes resId: Int) = lazyNotSynchronized { getDimension(resId) }

fun View.getDimensionPixelSize(@DimenRes dimenResId: Int) = resources.getDimensionPixelSize(dimenResId)
fun View.lazyDimensionPixelSize(@DimenRes resId: Int) = lazyNotSynchronized { getDimensionPixelSize(resId) }

fun View.lazyFont(@FontRes fontId: Int) = resources.getFont(fontId)