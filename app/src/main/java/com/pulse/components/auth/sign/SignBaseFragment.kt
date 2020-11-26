package com.pulse.components.auth.sign

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import com.pulse.R
import com.pulse.components.auth.AuthViewModel
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.debugIfElse
import com.pulse.core.extensions.lazyGetString
import com.pulse.core.extensions.setSoftInputMode
import com.pulse.core.extensions.sharedGraphViewModel

abstract class SignBaseFragment(@LayoutRes layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val phoneHint by lazyGetString(debugIfElse({ R.string.authPhoneDebugHint }, { R.string.authPhoneHint }))

    protected val vm: AuthViewModel by sharedGraphViewModel(R.id.auth_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}