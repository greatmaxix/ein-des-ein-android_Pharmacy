package com.pulse.core.base.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pulse.R
import com.pulse.core.base.fragment.BaseFragment
import com.pulse.core.base.helper.UiHelper
import com.pulse.core.extensions.observe
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseMVVMFragment<VM : BaseViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModelClass: KClass<VM>) :
    BaseFragment(layoutResourceId), IBind {

    protected open val viewModel: VM by lazy { getViewModel(clazz = viewModelClass) }
    protected val uiHelper by lazy { UiHelper(requireActivity()) }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.theme.postEvent(requireActivity().theme)
        onBindBase()

        onBindEvents()
        onBindStates()
    }

    @CallSuper
    protected open fun onBindBase() = with(lifecycleScope) {
        observe(viewModel.loadingState, uiHelper::showLoading)
        observe(viewModel.messageEvent) { it.contentOrNull?.let(uiHelper::showMessage) }
        observe(viewModel.navEvent) { it.contentOrNull?.let(navController::navigate) }
    }

    /**
     * Here we may bind our observers for events as Navigation, Show dialog, Toast ect.
     * This method will be executed after parent [onCreateView] method
     */
    override fun onBindEvents() {
        //Optional
    }

    /**
     * Here we may bind our observers for state changes as updating view ect.
     * This method will be executed after parent [onCreateView] method
     */
    override fun onBindStates() {
        //Optional
    }

    protected fun requestPermissions(
        firstPermission: String,
        vararg otherPermissions: String = arrayOf(),
        @StringRes openSettingsMessage: Int,
        @StringRes rationaleMessage: Int,
        @StringRes deniedMessage: Int,
        action: () -> Unit
    ) {
        val request = permissionsBuilder(firstPermission, *otherPermissions).build()
        request.addListener { result ->
            when {
                result.anyPermanentlyDenied() -> uiHelper.openSettingsMessage(getString(openSettingsMessage))
                result.anyShouldShowRationale() -> {
                    uiHelper.showDialog(getString(rationaleMessage)) {
                        cancelable = false
                        positive = getString(R.string.common_okButton)
                        positiveAction = { request.send() }
                        negative = getString(R.string.cancel)
                    }
                }
                result.anyDenied() -> uiHelper.showMessage(getString(deniedMessage))
                result.allGranted() -> action()
            }
        }
        request.send()
    }

    // TODO change it
    protected fun <T> observeSavedStateHandler(key: String, onChanged: (T) -> Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)?.let { it.observe(viewLifecycleOwner, onChanged) }
    }
}