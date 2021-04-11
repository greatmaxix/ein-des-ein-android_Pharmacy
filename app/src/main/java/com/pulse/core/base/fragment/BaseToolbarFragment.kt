package com.pulse.core.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.extensions.findViewById
import com.pulse.core.extensions.observe
import reactivecircus.flowbinding.appcompat.itemClicks
import reactivecircus.flowbinding.appcompat.navigationClicks
import kotlin.reflect.KClass

abstract class BaseToolbarFragment<VM : BaseViewModel>(@LayoutRes resId: Int, viewModelClass: KClass<VM>, @MenuRes private val menu: Int? = null) :
    BaseMVVMFragment<VM>(resId, viewModelClass) {

    private val label
        get() = navController.currentDestination?.label

    private var toolbar: Toolbar? = null
        set(value) {
            field = value?.apply(::initToolbar)
        }

    protected val menuItemsFlow
        get() = toolbar!!.itemClicks()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar = findViewById(R.id.toolbar)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initToolbar(toolbar: Toolbar) = with(toolbar) {
        title = label
        this@BaseToolbarFragment.menu?.let(::inflateMenu)
    }

    protected fun showBackButton(@DrawableRes drawable: Int = R.drawable.ic_arrow_back) {
        toolbar?.navigationIcon = ContextCompat.getDrawable(requireContext(), drawable)
    }

    protected fun setTitle(title: String) {
        toolbar?.title = title
    }

    protected fun changeMenuItemVisibility(@IdRes itemId: Int, isVisible: Boolean): Boolean {
        toolbar?.menu?.findItem(itemId)?.let {
            it.isVisible = isVisible
            return true
        } ?: return false
    }

    override fun onBindBase() {
        super.onBindBase()
        with(lifecycleScope) {
            toolbar?.apply {
                observe(navigationClicks()) {
                    onClickNavigation()
                }
            }
        }
    }

    /**
     * Here we may observing for navigation actions
     * This method will be executed after parent [onBindEvents] method
     */
    protected open fun onClickNavigation() {
        hideKeyboardOrPopBackStack()
    }
}