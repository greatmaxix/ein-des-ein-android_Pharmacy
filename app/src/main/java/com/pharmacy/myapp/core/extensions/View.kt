package com.pharmacy.myapp.core.extensions

import android.animation.*
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isEmpty
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.core.widget.TextViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textfield.TextInputLayout
import com.pharmacy.myapp.R
import kotlinx.android.synthetic.main.item_cart_product.view.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.math.BigDecimal

fun View.freeze(duration: Long = 300) {
    isEnabled = false
    Handler().postDelayed({ isEnabled = true }, duration)
}

inline fun View.onClick(crossinline f: () -> Unit) {
    setOnClickListener { f.invoke() }
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
        val params = layoutParams
        params.height = height
        params.width = width
        layoutParams = params
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
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.animateGone(duration: Long = 100) {
    animate().setDuration(duration).alpha(0f).withEndAction {
        visibility = View.GONE
    }
}

fun View.animateGoneIfNot(duration: Long = 100) {
    if (visibility != View.GONE) {
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

fun View.hideKeyboard() = context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0).also { clearFocus() }

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

fun View.colorCompat(colorRes: Int) = context.getCompatColor(colorRes)

@RequiresApi(Build.VERSION_CODES.M)
fun View.colorFrom(@ColorRes colorRes: Int) = context.getColor(colorRes)

fun View.moveHorizontal(progress: Float, alpha: Float? = null) {
    translationX = progress
    alpha?.let { this.alpha = it }
}

fun View.distanceTo(view: View): Int {
    val firstPosition = IntArray(2)
    val secondPosition = IntArray(2)

    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    getLocationOnScreen(firstPosition)
    view.getLocationOnScreen(secondPosition)

    val b = view.measuredHeight + firstPosition[1]
    val t = secondPosition[1]
    return Math.abs(b - t)
}

fun View.animateRotate() {
    AnimatorInflater.loadAnimator(context, R.animator.animator_rotate).apply {
        setTarget(this@animateRotate)
        start()
    }
}

fun View.colorValueAnimator(from: Int, to: Int, duration: Long, onUpdate: (Int) -> Unit): View {
    ValueAnimator.ofObject(ArgbEvaluator(), from, to).apply {
        addUpdateListener { onUpdate(it.animatedValue as Int) }
        this.duration = duration
        start()
    }
    return this
}

val View.toTransitionGroup
    get() = this to transitionName

fun View.setDebounceOnClickListener(interval: Long = 400, listener: View.() -> Unit) {
    val lastClickMap = mutableMapOf<Int, Long>()
    setOnClickListener { v ->
        val currentTimestamp = SystemClock.uptimeMillis()
        if (currentTimestamp - lastClickMap.getOrDefault(v.id, 0) > interval) run { listener.invoke(v) }
        lastClickMap[v.id] = currentTimestamp
    }
}

fun View.setTopRoundCornerBackground() {
    val radius = resources.getDimension(R.dimen._8sdp)
    val appearanceModel = ShapeAppearanceModel()
        .toBuilder()
        .setTopRightCorner(CornerFamily.ROUNDED, radius)
        .setTopLeftCorner(CornerFamily.ROUNDED, radius)
        .build()

    val shape = MaterialShapeDrawable(appearanceModel)
        .apply {
            shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
            elevation = resources.getDimension(R.dimen._4sdp)
            setTint(ContextCompat.getColor(context, R.color.colorGlobalWhite))
            paintStyle = Paint.Style.FILL
        }

    ViewCompat.setBackground(this, shape)
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

fun TextView.compatTextColor(@ColorRes colorResId: Int) {
    setTextColor(colorCompat(colorResId))
}

@RequiresApi(Build.VERSION_CODES.M)
fun TextView.textColor(@ColorRes colorResId: Int): TextView {
    setTextColor(colorFrom(colorResId))
    return this
}

suspend fun TextView.setPrecomputedTextCompat(text: String) =
    TextViewCompat.setPrecomputedText(this, withContext(Default) { createPrecomputedTextCompat(text) })

fun TextView.createPrecomputedTextCompat(text: String): PrecomputedTextCompat =
    PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))

fun Resources.getBitmapDrawableFromVectorDrawable(@DrawableRes drawableRes: Int) = getDrawable(drawableRes, null).let {
    BitmapDrawable(this, Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
    })
}

/*fun TextInputLayout.setText(text: String?) {
    editText?.run {
        setText(text)
    }
}*/

fun TextInputLayout.text() = editText?.text() ?: ""

fun EditText.updateText(newString: String?) {
    if (text().trim().isEmpty() || newString != text().trim()) {
        newString?.let {
            setText(it)
            setSelection(it.length)
        }
    }
}

fun EditText.updateText(newString: Any?) {
    setText(newString.toString())
    setSelection(text.length)
}

fun EditText.isEmpty() = text().isEmpty()

fun EditText.textToBigDecimal() = text().toBigDecimal()

fun EditText.setText(bigDecimal: BigDecimal?) = setText(bigDecimal.toString())

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attach: Boolean = false) = context.inflate(layoutRes, this, attach)

fun ViewGroup.getDimensionPixelSize(@DimenRes dimenResId: Int) = resources.getDimensionPixelSize(dimenResId)

fun ViewGroup.getDimension(@DimenRes dimenResId: Int) = resources.getDimension(dimenResId)

val ViewGroup.lastChild: View
    get() = getChildAt(childCount - 1)

fun RecyclerView.addDrawableItemDivider(@DrawableRes drawableRes: Int) {
    context.getCompatDrawable(drawableRes)?.let {
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(it)
        })
    }
}

fun Toolbar.setMenu(@MenuRes menu: Int, itemClick: Toolbar.OnMenuItemClickListener? = null) {
    if (this.menu.isEmpty()) {
        inflateMenu(menu)
        setOnMenuItemClickListener(itemClick)
    }
}

val Toolbar.titleView: View
    get() = getChildAt(0)

fun NestedScrollView.scrollToBottom() {
    scrollBy(0, lastChild.bottom + paddingBottom - (scrollY + height))
}

fun NestedScrollView.scrollToTop() {
    scrollTo(0, 0)
}