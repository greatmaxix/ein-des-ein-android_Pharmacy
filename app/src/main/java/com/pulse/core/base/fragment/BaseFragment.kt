package com.pulse.core.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.pulse.R
import com.pulse.core.dsl.Bar
import com.pulse.core.extensions.*
import com.pulse.core.general.behavior.IBehavior
import com.pulse.core.general.interfaces.MessagesCallback
import com.pulse.core.general.interfaces.ProgressCallback

abstract class BaseFragment(@LayoutRes private val layoutResourceId: Int) : Fragment(layoutResourceId) {

    protected var progressCallback: ProgressCallback? = null
        private set
    protected var messageCallback: MessagesCallback? = null
        private set
    protected var toolbar: Toolbar? = null
        private set(value) {
            field = value
            initToolbar()
        }

    protected val navController by lazy { findNavController() }

    private val behaviors = mutableListOf<IBehavior?>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProgressCallback) {
            progressCallback = context
        }
        if (context is MessagesCallback) {
            messageCallback = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar = view.findViewById(R.id.toolbar)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initToolbar() {
        toolbar?.title = label
        toolbar?.setNavigationOnClickListener { navigationBack() }
    }

    protected fun setToolbarTitle(title: String?) {
        toolbar?.title = title?.wrapHtml
    }

    protected fun changeNavigationIcon(@DrawableRes drawable: Int) {
        asyncWithContext({ context?.getDrawable(drawable) }, { toolbar?.navigationIcon = this })
    }

    open fun navigationBack() {
        if (isKeyboardOpen) {
            hideKeyboard()
        } else {
            navController.popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    protected fun <T : IBehavior> attachBehavior(behavior: T) = behavior.also {
        behaviors.add(it)
    }

    @CallSuper
    override fun onDestroy() {
        behaviors.forEach { it?.detach() }
        behaviors.clear()
        super.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        progressCallback = null
        messageCallback = null
        super.onDetach()
    }

    protected fun attachBackPressCallback(enabled: Boolean = true, action: OnBackPressedCallback.() -> Unit) {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                action(this)
            }
        })
    }

    protected fun initMenu(@MenuRes menu: Int, itemClick: Toolbar.OnMenuItemClickListener? = null) =
        toolbar?.setMenu(menu, itemClick ?: Toolbar.OnMenuItemClickListener { onOptionsItemSelected(it) })

    protected fun setToolbarColorActivityLevel(@ColorRes colorTo: Int, @ColorRes colorFrom: Int? = null) {
        baseActivity.setToolbarColor(colorTo, colorFrom)
    }

    protected fun setToolbarContentColorActivityLevel(@ColorRes colorRes: Int) {
        baseActivity.setToolbarContentColor(colorRes)
    }

    protected fun initMenuActivityLevel(@MenuRes menu: Int, itemClick: Toolbar.OnMenuItemClickListener? = null) {
        baseActivity.initMenu(menu, itemClick)
    }

    protected fun showBackButtonActivityLevel() {
        baseActivity.showBackButton()
    }

    protected fun showBackButton(@DrawableRes drawable: Int = R.drawable.ic_arrow_back, navigation: ((View) -> Unit)? = null) {
        changeNavigationIcon(drawable)
        navigation?.let { toolbar?.setNavigationOnClickListener(it::invoke) }
    }

    protected fun changeBarColors(block: Bar.() -> Unit) {
        Bar().apply(block).apply {
            lightStatusBar?.let(::setLightStatusBar)
            statusBarColorRes?.let(::setStatusBarColor)
            navigationBarColorRes?.let(::setNavigationBarColor)
        }
    }

    protected fun doNav(directions: NavDirections) = navController.onNavDestinationSelected(directions)

    protected fun doNav(@IdRes destId: Int, args: Bundle? = null) = navController.onNavDestinationSelected(destId, args)

    protected fun doNav(@IdRes destId: Int, startDestination: Int, args: Bundle? = null, inclusive: Boolean = false) =
        navController.onNavDestinationSelected(destId, args, startDestination, inclusive)

    protected fun navigateUp() = navController.navigateUp()

    protected val label
        get() = navController.currentDestination?.label

    protected val Int.toPixels
        get() = dimensionPixelSize(this)

    protected val Int.toColor
        get() = getColor(this)

    protected val Int.toDrawable
        get() = getDrawable(this)

    protected fun Int.toString(vararg args: Any?) = getString(this, *args)

    protected val Int.toString
        get() = getString(this)
}