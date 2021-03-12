package com.pulse.core.extensions

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.pulse.BuildConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "${BuildConfig.APPLICATION_ID}_prefs")

val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun Context.inflate(@LayoutRes resId: Int, root: ViewGroup? = null, attachToRoot: Boolean = false): View =
    inflater.inflate(resId, root, attachToRoot)

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(@StringRes strResId: Int) = toast(getString(strResId))

val Context.windowManager: WindowManager?
    get() = getSystemService()

val Context.notificationManager: NotificationManager?
    get() = getSystemService()

val Context.inputMethodManager: InputMethodManager?
    get() = getSystemService()

val Context.activityManager: ActivityManager?
    get() = getSystemService()

val Context.screenHeight
    get() = Point().run {
        windowManager?.defaultDisplay?.getSize(this)
        y
    }

val Context.screenWidth
    get() = Point().run {
        windowManager?.defaultDisplay?.getSize(this)
        x
    }

val Context.statusBarHeight
    get() = getDimensionPixelSize("status_bar_height")

val Context.navigationBarHeight
    get() = getDimensionPixelSize("navigation_bar_height")

val Context.actionBarSize
    get() = TypedValue().run {
        if (theme.resolveAttribute(android.R.attr.actionBarSize, this, true)) {
            TypedValue.complexToDimensionPixelSize(data, resources.displayMetrics)
        } else {
            0
        }
    }

inline fun <reified T> Context.toIntent(flags: Int?) = Intent(this, T::class.java).apply {
    flags?.let { this.flags = it }
}

inline fun <reified T> Context.toIntent() = Intent(this, T::class.java)

inline fun <reified T> Context.startActivity(flags: Int?) = startActivity(toIntent<T>(flags))

fun Context.getCompatDrawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)
fun Context.getDimenByName(name: String, defPackage: String = "android") = resources.getDimenByName(name, defPackage)
fun Context.getDimensionPixelSize(name: String) = resources.getDimensionPixelSize(getDimenByName(name))
fun Context.getStringByName(name: String) = resources.getStringByName(name, packageName)
fun Context.getDrawableByName(name: String) = resources.getDrawableByName(name, packageName)

fun Context.convertDpToPx(dp: Float) = resources.convertDpToPx(dp)
fun Context.convertPxToDp(px: Float) = resources.convertPxToDp(px)

@Suppress("DEPRECATION") // TODO find solution for API >= 30
fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
    return activityManager
        ?.getRunningServices(Integer.MAX_VALUE)
        ?.any { it.service.className == service.name } ?: false
}