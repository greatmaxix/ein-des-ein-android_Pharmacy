package com.pulse.core.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.ViewGroup
import android.view.Window.ID_ANDROID_CONTENT
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.fragment.app.*
import androidx.navigation.fragment.NavHostFragment
import com.pulse.R
import com.pulse.core.base.fragment.dialog.AlertDialogData
import com.pulse.core.base.fragment.dialog.AlertDialogDataRes
import com.pulse.core.base.fragment.dialog.AlertDialogFragment
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

fun Activity.setLightStatusBar(light: Boolean) = window.setLightStatusBar(light)

fun Activity.setStatusBarColor(@ColorRes colorRes: Int, withAnim: Boolean = false) = window.setStatusBarColorInt(getColor(colorRes), withAnim)

fun Activity.setNavigationBarColor(@ColorRes colorRes: Int, withAnim: Boolean = false) = window.setNavigationBarColorInt(getColor(colorRes), withAnim)

fun Activity.refreshActivity() {
    finish()
    startActivity(intent.newTask().clearTask())
}

fun FragmentActivity.showAlert(message: String, block: AlertDialogData.() -> Unit, fm: FragmentManager = supportFragmentManager) =
    AlertDialogFragment.newInstance(message, block).show(fm)

fun FragmentActivity.showAlertRes(message: String, block: AlertDialogDataRes.() -> Unit, fm: FragmentManager = supportFragmentManager) =
    AlertDialogFragment.newInstance(this, message, block).show(fm)

fun FragmentActivity.lazyNavController(@IdRes resId: Int) = lazyNotSynchronized {
    try {
        (supportFragmentManager.findFragmentById(resId) as NavHostFragment).navController
    } catch (e: IllegalArgumentException) {
        throw IllegalArgumentException("${this::class.java.simpleName} does not use \"navController\"")
    }
}

fun FragmentActivity.setFragment(fragment: Fragment, containerId: Int = R.id.container, needBackStack: Boolean = false, defaultNavHost: Boolean = false) =
    supportFragmentManager.commit {
        supportFragmentManager.findFragmentById(containerId)?.let(::detach)
        replaceTransaction(fragment, containerId, needBackStack, defaultNavHost)
    }

fun FragmentActivity.setFragmentNow(fragment: Fragment, containerId: Int = R.id.container, needBackStack: Boolean = false, defaultNavHost: Boolean = false) =
    supportFragmentManager.commitNow {
        supportFragmentManager.findFragmentById(containerId)?.let(::detach)
        replaceTransaction(fragment, containerId, needBackStack, defaultNavHost)
    }

private fun FragmentTransaction.replaceTransaction(fragment: Fragment, containerId: Int = R.id.container, needBackStack: Boolean = false, defaultNavHost: Boolean = false) {
    replace(containerId, fragment)
    if (needBackStack) {
        addToBackStack(null)
    }
    if (defaultNavHost) {
        setPrimaryNavigationFragment(fragment)
    }
}