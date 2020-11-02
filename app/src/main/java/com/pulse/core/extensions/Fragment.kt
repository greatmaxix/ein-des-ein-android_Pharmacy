package com.pulse.core.extensions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_VIEW
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pulse.BuildConfig
import com.pulse.R
import com.pulse.core.base.BaseActivity
import com.pulse.core.base.fragment.dialog.AlertDialogData
import com.pulse.core.base.fragment.dialog.AlertDialogDataRes
import com.pulse.core.flow.CountDownFlow
import com.pulse.core.keyboard.KeyboardObserver
import com.pulse.search.SearchFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ViewModelParameter
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import timber.log.Timber
import java.util.*
import kotlin.reflect.KSuspendFunction0

fun Fragment.hideKeyboard() {
    view?.hideKeyboard()
}

fun Fragment.showKeyboard(editText: EditText) {
    inputMethodManager.showSoftInput(editText, SHOW_FORCED)
    editText.requestFocus()
}

val Fragment.activityRootView
    get() = requireActivity().rootView

val Fragment.isKeyboardOpen
    get() = requireActivity().isKeyboardOpen

val Fragment.isKeyboardNotOpen
    get() = !isKeyboardOpen

val Fragment.compatActivity
    get() = activity as AppCompatActivity

fun Fragment.doWithDelay(delay: Long, action: () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        delay(delay)
        action()
    }
}

fun Fragment.doWithDelay(delay: Int, action: () -> Unit) = doWithDelay(delay.toLong(), action)

fun Fragment.doWithContext(start: Context.() -> Unit) {
    context?.let {
        start(it)
    }
}

fun Fragment.doWithActivity(start: FragmentActivity.() -> Unit) {
    start(requireActivity())
}

val Fragment.screeHeight get() = requireActivity().screenHeight

val Fragment.screenWidth get() = requireActivity().screenWidth

val Fragment.statusBarHeight
    get() = requireContext().statusBarHeight

val Fragment.actionBarSize
    get() = requireActivity().actionBarSize

fun Fragment.dimension(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun Fragment.dimensionPixelSize(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)

val Fragment.baseActivity get() = requireActivity() as BaseActivity

fun Fragment.getColor(@ColorRes resId: Int) = requireContext().getCompatColor(resId)

fun Fragment.getDrawable(@DrawableRes resId: Int?) = resId?.let(requireContext()::getDrawable)

fun Fragment.getDrawableWithTint(@DrawableRes drawableResId: Int?, @ColorRes colorResId: Int) =
    getDrawable(drawableResId)?.apply { mutate().setTint(getColor(colorResId)) }

inline fun <FRAGMENT : Fragment> FRAGMENT.putArgs(argsBuilder: Bundle.() -> Unit): FRAGMENT =
    this.apply { arguments = Bundle().apply(argsBuilder) }

fun <FRAGMENT : Fragment> FRAGMENT.getIntArg(key: String, defValue: Int) = arguments?.getInt(key, defValue) ?: defValue

fun <FRAGMENT : Fragment> FRAGMENT.getStringArg(key: String, defValue: String?) = arguments?.getString(key, defValue) ?: defValue

fun Fragment.alert(message: String, block: AlertDialogData.() -> Unit) = requireActivity().alert(message, block)

fun Fragment.showAlert(message: String, block: AlertDialogData.() -> Unit) = requireActivity().showAlert(message, block, childFragmentManager)

fun Fragment.showAlert(@StringRes resId: Int, block: AlertDialogData.() -> Unit) = requireActivity().showAlert(getString(resId), block, childFragmentManager)

fun Fragment.showAlertRes(@StringRes resId: Int, block: AlertDialogDataRes.() -> Unit) = requireActivity().showAlertRes(getString(resId), block)

fun Fragment.showAlertRes(message: String, block: AlertDialogDataRes.() -> Unit) = requireActivity().showAlertRes(message, block, childFragmentManager)

fun Fragment.browser(url: String, block: (BrowserIntent.() -> Unit)? = null) = requireActivity().browser(url, block)

fun Fragment.browser(@StringRes url: Int, block: (BrowserIntent.() -> Unit)? = null) = requireActivity().browser(getString(url), block)

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

inline fun <reified T> Fragment.startActivityWithFinish(flags: Int) = startActivity<T>(flags).also { finishAffinity() }

fun Fragment.finishAffinity() = requireActivity().finishAffinity()

/*fun Fragment.startTimer(tick: ((Long) -> Unit)? = null, finish: (() -> Unit)? = null, allTime: Long = 61000, interval: Long = 1000) {
    lifecycle.addObserver(CountDownTimerLife(tick, finish, allTime, interval))
}*/

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun Fragment.startTimer(
    start: (() -> Unit)? = null,
    emit: ((Long) -> Unit)? = null,
    complete: (() -> Unit)? = null,
    millisInFuture: Long = 60000L,
    interval: Long = 1000L,
    delay: Long = 1000L
) {
    viewLifecycleOwner.lifecycleScope.launch(Main) {
        try {
            CountDownFlow(millisInFuture, interval, delay)
                .onStart { start?.invoke() }
                .onEach { emit?.invoke(it) }
                .onCompletion { complete?.invoke() }
                .collect()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}

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

@KoinApiExtension
fun Fragment.setFragmentIfNeed(fragment: Fragment, @IdRes containerId: Int = R.id.container, needBackStack: Boolean = false) {
    if (findFragment<SearchFragment>(containerId) == null) {
        setFragment(fragment, containerId, needBackStack)
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
        childFragmentManager.inTransaction { add(containerId, fragment) }
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
    result(withContext(Dispatchers.Default) { async() })
}

fun Fragment.launch(action: KSuspendFunction0<Unit>) = viewLifecycleOwner.lifecycleScope.launch { action() }

fun Fragment.launch(action: (CoroutineScope) -> Unit) = viewLifecycleOwner.lifecycleScope.launch { action(this) }

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

val Fragment.currentTopFragment
    get() = try {
        childFragmentManager.fragments.first()
    } catch (e: NoSuchElementException) {
        Timber.e("No fragments in childFragmentManager: $e")
        null
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

inline fun debug(code: () -> Unit) {
    if (BuildConfig.DEBUG) {
        code()
    }
}