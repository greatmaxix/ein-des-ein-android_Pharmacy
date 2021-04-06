package com.pulse.core.extensions

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_VIEW
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pulse.R
import com.pulse.core.base.fragment.dialog.AlertDialogData
import com.pulse.core.base.fragment.dialog.AlertDialogDataRes
import com.pulse.core.keyboard.KeyboardObserver
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ViewModelParameter
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

fun <T : View> Fragment.findViewById(@IdRes id: Int) = view?.findViewById<T>(id)

fun Fragment.hideKeyboard() {
    view?.hideKeyboard()
}

val Fragment.activityRootView
    get() = requireActivity().rootView

val Fragment.isKeyboardOpen
    get() = requireActivity().isKeyboardOpen

val Fragment.isKeyboardNotOpen
    get() = !isKeyboardOpen

fun Fragment.doWithDelay(delay: Long, action: () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        delay(delay)
        action()
    }
}

fun Fragment.doWithDelay(delay: Int, action: () -> Unit) = doWithDelay(delay.toLong(), action)

fun Fragment.getDimension(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun Fragment.getDimensionPixelSize(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)

fun Fragment.getColor(@ColorRes resId: Int) = requireContext().getColor(resId)

fun Fragment.getDrawable(@DrawableRes resId: Int?) = resId?.let(requireContext()::getDrawable)

fun Fragment.getDrawableWithTint(@DrawableRes drawableResId: Int?, @ColorRes colorResId: Int) =
    getDrawable(drawableResId)?.apply { mutate().setTint(getColor(colorResId)) }

inline fun <FRAGMENT : Fragment> FRAGMENT.putArgs(argsBuilder: Bundle.() -> Unit): FRAGMENT =
    this.apply { arguments = Bundle().apply(argsBuilder) }

fun <FRAGMENT : Fragment> FRAGMENT.getIntArg(key: String, defValue: Int) = arguments?.getInt(key, defValue) ?: defValue

fun <FRAGMENT : Fragment> FRAGMENT.getStringArg(key: String, defValue: String?) = arguments?.getString(key, defValue) ?: defValue

fun Fragment.showAlert(message: String, block: AlertDialogData.() -> Unit) = requireActivity().showAlert(message, block, childFragmentManager)

fun Fragment.showAlert(@StringRes resId: Int, block: AlertDialogData.() -> Unit) = requireActivity().showAlert(getString(resId), block, childFragmentManager)

fun Fragment.showAlertRes(@StringRes resId: Int, block: AlertDialogDataRes.() -> Unit) = requireActivity().showAlertRes(getString(resId), block)

fun Fragment.showAlertRes(message: String, block: AlertDialogDataRes.() -> Unit) = requireActivity().showAlertRes(message, block, childFragmentManager)

fun Fragment.showDial(number: String) = startActivity(Intent(ACTION_DIAL, String.format("tel:%s", Uri.encode(number)).toUri()))

fun Fragment.showDirection(lat: Double, lng: Double) = startActivity(Intent(ACTION_VIEW, "google.navigation:q=${lat},${lng}&mode=w".toUri()))

fun Fragment.getFragmentTag(suffix: String? = null): String = this::class.java.simpleName + (suffix ?: "")

fun Fragment.unregisterReceiver(receiver: BroadcastReceiver?) {
    if (receiver != null) requireActivity().unregisterReceiver(receiver)
}

fun Fragment.registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter) {
    if (receiver != null) requireActivity().registerReceiver(receiver, filter)
}

fun Fragment.setStatusBarColor(@ColorRes colorRes: Int, withAnim: Boolean = false) {
    requireActivity().setStatusBarColor(colorRes, withAnim)
}

fun Fragment.setNavigationBarColor(@ColorRes colorRes: Int, withAnim: Boolean = false) {
    requireActivity().setNavigationBarColor(colorRes, withAnim)
}

fun Fragment.setLightStatusBar(light: Boolean) {
    requireActivity().setLightStatusBar(light)
}

fun Fragment.addBackPressListener(onBackPressed: OnBackPressedCallback.() -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true, onBackPressed)
}

fun Fragment.backPress() = requireActivity().onBackPressed()

inline fun <reified T> Fragment.startActivity(flags: Int) = requireContext().startActivity<T>(flags)

val Fragment.keyboardObserver
    get() = KeyboardObserver(view).apply { viewLifecycleOwner.lifecycle.addObserver(this) }

fun <T : Fragment> Fragment.setFragment(
    fragment: Class<T>?,
    args: Bundle? = null,
    @IdRes containerId: Int = R.id.container,
    needBackStack: Boolean = false
) = fragment?.let {
    childFragmentManager.commit {
        setCustomAnimations(R.anim.nav_enter_anim, R.anim.nav_exit_anim, R.anim.nav_enter_pop_anim, R.anim.nav_exit_pop_anim)
        replace(containerId, it, args, it::class.java.simpleName)
        if (needBackStack) {
            addToBackStack(fragment::class.java.simpleName)
        }
    }
}

fun Fragment.setFragment(fragment: Fragment, @IdRes containerId: Int = R.id.container, needBackStack: Boolean = false) =
    with(childFragmentManager) {
        commit {
            findFragmentById(containerId)?.let(::detach)
            replace(containerId, fragment)
            if (needBackStack) {
                addToBackStack(null)
            }
        }
    }

fun Fragment.addFragment(fragment: Fragment, @IdRes containerId: Int = R.id.container) {
    if (childFragmentManager.findFragmentById(containerId) == null) {
        childFragmentManager.commit {
            add(containerId, fragment)
        }
    }
}

inline fun <reified F : Fragment> Fragment.findFragment(@IdRes containerId: Int = R.id.container): F? =
    childFragmentManager.findFragmentById(containerId)?.run { if (this is F) this else null }

val Fragment.inputMethodManager
    get() = requireContext().inputMethodManager

val Fragment.window: Window
    get() = requireActivity().window

fun Fragment.setSoftInputMode(mode: Int) = window.setSoftInputMode(mode)

fun Fragment.setWindowBackground(@DrawableRes resId: Int?) = window.setBackgroundDrawable(getDrawable(resId))

fun Fragment.setAsyncWindowBackground(@DrawableRes resId: Int?) = asyncWithContext({ getDrawable(resId) }, window::setBackgroundDrawable)

fun <T> Fragment.asyncWithContext(async: () -> T, result: T.() -> Unit) = viewLifecycleOwner.lifecycleScope.launch {
    result(withContext(Default) { async() })
}

fun Fragment.sendEmail(email: String, subject: String? = null, message: String? = null, @StringRes intentTitle: Int) =
    run {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            message?.let { putExtra(Intent.EXTRA_TEXT, it) }
            subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
        }
        startActivity(Intent.createChooser(intent, getString(intentTitle)))
    }

inline fun <reified VM : ViewModel> Fragment.sharedGraphViewModel(
    @IdRes navGraphId: Int,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = lazy {
    val store = findNavController().getViewModelStoreOwner(navGraphId).viewModelStore
    getKoin().getViewModel(ViewModelParameter(VM::class, qualifier, parameters, Bundle(), store, null))
}

fun <T> Fragment.notifySavedStateHandle(key: String, value: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun Fragment.lazyGetFont(@FontRes resId: Int) = lazyNotSynchronized { resources.getFont(resId) }
fun Fragment.lazyGetString(@StringRes resId: Int) = lazyNotSynchronized { resources.getString(resId) }
fun Fragment.lazyGetString(@StringRes resId: Int, vararg args: Any?) = lazyNotSynchronized { resources.getString(resId, *args) }
fun Fragment.lazyGetDimensionPixelSize(@DimenRes resId: Int) = lazyNotSynchronized { getDimensionPixelSize(resId) }