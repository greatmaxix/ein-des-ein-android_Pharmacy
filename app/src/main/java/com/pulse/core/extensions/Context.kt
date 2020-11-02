package com.pulse.core.extensions

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.pulse.BuildConfig

@ColorInt
fun Context.getCompatColor(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Context.getCompatDrawable(@DrawableRes drawableRes: Int): Drawable? = ContextCompat.getDrawable(this, drawableRes)

fun Context.inflate(@LayoutRes resId: Int, root: ViewGroup? = null, attachToRoot: Boolean = false): View =
    inflater.inflate(resId, root, attachToRoot)

fun Context.convertDpToPx(dp: Float) = applyDimension(COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

fun Context.convertPxToDp(px: Float?) = px?.let { px / (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT) }

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(@StringRes strResId: Int) = toast(getString(strResId))

val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)

val Context.windowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

val Context.notificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

val Context.inputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.screenHeight
    get() = Point().run {
        windowManager.defaultDisplay.getSize(this)
        y
    }

val Context.screenWidth
    get() = Point().run {
        windowManager.defaultDisplay.getSize(this)
        x
    }

val Context.statusBarHeight
    get() = dimenByNameAsPixel("status_bar_height")

val Context.navigationBarHeight
    get() = dimenByNameAsPixel("navigation_bar_height")


fun Context.themeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

inline fun <reified T> Context.toIntent(flags: Int?) = Intent(this, T::class.java).apply {
    flags?.let { this.flags = it }
}

inline fun <reified T> Context.toIntent() = Intent(this, T::class.java)

inline fun <reified T> Context.startActivity(flags: Int?) {
    startActivity(toIntent<T>(flags))
}

fun Context.dimenByNameAsPixel(name: String) = resources.getDimensionPixelSize(dimenByName(name))

fun Context.dimenByName(name: String, defType: String = "dimen", defPackage: String = "android") = resources.getIdentifier(name, defType, defPackage)

fun Context.stringByName(name: String) = resources.getIdentifier(name, "string", packageName)

inline fun Context.debug(code: () -> Unit) {
    if (BuildConfig.DEBUG) {
        code()
    }
}

@Suppress("DEPRECATION") // TODO find solution for API >= 30
fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
    return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name }
}