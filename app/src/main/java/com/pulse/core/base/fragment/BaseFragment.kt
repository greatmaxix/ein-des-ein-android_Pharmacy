package com.pulse.core.base.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pulse.core.extensions.hideKeyboard
import com.pulse.core.extensions.isKeyboardOpen

abstract class BaseFragment(@LayoutRes layoutResourceId: Int) : Fragment(layoutResourceId) {

    protected val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    protected fun hideKeyboardOrPopBackStack() {
        when {
            requireView().isKeyboardOpen -> hideKeyboard()
            else -> requireActivity().onBackPressed() // backPress needed for execute MainActivity back press logic
        }
    }

    // TODO review this case
    open fun navigationBack() {
        if (isKeyboardOpen) {
            hideKeyboard()
        } else {
            if (!navController.popBackStack()) {
                requireActivity().finish()
            }
        }
    }

    // TODO implement using @callbackFlow
    protected fun attachBackPressCallback(enabled: Boolean = true, action: OnBackPressedCallback.() -> Unit) =
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() = action(this)
        })

    /**
     * Here we may init UI components.
     * This method will be executed after parent [onCreateView] method
     */
    protected abstract fun initUI()
}