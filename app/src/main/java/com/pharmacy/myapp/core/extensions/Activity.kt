package com.pharmacy.myapp.core.extensions

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window.ID_ANDROID_CONTENT
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.BaseActivity.Companion.ANIM_ENTER
import com.pharmacy.myapp.core.base.BaseActivity.Companion.ANIM_EXIT
import com.pharmacy.myapp.core.base.fragment.dialog.AlertDialogData
import com.pharmacy.myapp.core.base.fragment.dialog.AlertDialogDataRes
import com.pharmacy.myapp.core.base.fragment.dialog.AlertDialogFragment
import kotlin.math.roundToInt

val Activity.rootView: ViewGroup
    get() = findViewById(ID_ANDROID_CONTENT)

fun Activity.hideKeyboard() = rootView.hideKeyboard()

val Activity.isKeyboardOpen: Boolean
    get() {
        val visibleBounds = Rect()
        val view = rootView.apply { getWindowVisibleDisplayFrame(visibleBounds) }
        return view.height - visibleBounds.height() > convertDpToPx(50F).roundToInt()
    }

val Activity.isKeyboardNotOpen
    get() = !isKeyboardOpen

val Activity.softInputMode get() = window?.attributes?.softInputMode

fun Activity.setStatusBarColor(@ColorRes colorRes: Int, withAnim: Boolean = false) {
    setStatusBarColorInt(getCompatColor(colorRes), withAnim)
}

fun Activity.setStatusBarColorInt(@ColorInt color: Int, withAnim: Boolean = false) {
    if (withAnim) {
        val colorFrom = window.statusBarColor
        animatorMedium { window.statusBarColor = colorFrom.mixColorWith(color, it) }
    } else {
        window.statusBarColor = color
    }
}

fun Activity.setLightStatusBar(light: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = window.decorView.systemUiVisibility
        val lightStatusFlag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = if (light) flags or lightStatusFlag else flags xor lightStatusFlag
    }
}

fun Activity.animatorMedium(tick: (Float) -> Unit) {
    ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener { tick.invoke(it.animatedFraction) }
        duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        start()
    }
}

fun Activity.browser(url: String, block: (BrowserIntent.() -> Unit)? = null) {
    val builder = BrowserIntent()
        .setToolbarColor(themeColor(android.R.attr.colorPrimary))
        .setStartAnimations(this, ANIM_ENTER, ANIM_EXIT)
        .setExitAnimations(this, ANIM_ENTER, ANIM_EXIT)
    block?.invoke(builder)
    builder.build().launchUrl(this, url.toUri())
}

fun Activity.browser(@StringRes url: Int, block: (BrowserIntent.() -> Unit)? = null) = browser(getString(url), block)

fun Activity.refreshActivity() {
    finish()
    startActivity(intent.newTask().clearTask())
}

val Activity.actionBarSize
    get() = TypedValue().run {
        return@run if (theme.resolveAttribute(android.R.attr.actionBarSize, this, true)) {
            TypedValue.complexToDimensionPixelSize(this.data, resources.displayMetrics)
        } else {
            0
        }
    }

fun Activity.alert(message: String, block: AlertDialogData.() -> Unit) = AlertDialogData().apply(block).run {
    AlertDialogFragment.newInstance(title, message, positive, negative)
        .apply {
            setNegativeListener(DialogOnClick { dialog, _ ->
                negativeAction?.invoke()
                dialog.dismiss()
            })
            setPositiveListener(DialogOnClick { _, _ -> positiveAction?.invoke() })
            isCancelable = cancelable
        }
}

fun Activity.alertRes(message: String, block: AlertDialogDataRes.() -> Unit) = AlertDialogDataRes().apply(block).run {
    AlertDialogFragment.newInstance(title?.let { getString(it) }, message, positive?.let { getString(it) }, negative?.let { getString(it) })
        .apply {
            setNegativeListener(DialogOnClick { dialog, _ ->
                negativeAction?.invoke()
                dialog.dismiss()
            })
            setPositiveListener(DialogOnClick { _, _ -> positiveAction?.invoke() })
            isCancelable = cancelable
        }
}

fun FragmentActivity.showAlert(message: String, block: AlertDialogData.() -> Unit) =
    AlertDialogData().apply(block).run { alert(message, block).show(supportFragmentManager) }

fun FragmentActivity.showAlertRes(message: String, block: AlertDialogDataRes.() -> Unit) =
    AlertDialogDataRes().apply(block).run { alertRes(message, block).show(supportFragmentManager) }

fun FragmentActivity.setFragment(fragment: Fragment, containerId: Int = R.id.container, needBackStack: Boolean = false, defaultNavHost: Boolean = false) {
    supportFragmentManager.commit{
        supportFragmentManager.findFragmentById(containerId)?.let(::detach)
        replace(containerId, fragment)
        if (needBackStack) {
            addToBackStack(null)
        }
        if (defaultNavHost) {
            setPrimaryNavigationFragment(fragment)
        }
    }
}

fun FragmentActivity.setFragmentNow(fragment: Fragment, containerId: Int = R.id.container, needBackStack: Boolean = false, defaultNavHost: Boolean = false) {
    supportFragmentManager.commitNow {
        supportFragmentManager.findFragmentById(containerId)?.let(::detach)
        replace(containerId, fragment)
        if (needBackStack) {
            addToBackStack(null)
        }
        if (defaultNavHost) {
            setPrimaryNavigationFragment(fragment)
        }
    }
}