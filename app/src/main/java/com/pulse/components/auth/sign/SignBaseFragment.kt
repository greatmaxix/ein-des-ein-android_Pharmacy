package com.pulse.components.auth.sign

import androidx.annotation.LayoutRes
import com.pulse.R
import com.pulse.components.auth.AuthViewModel
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.sharedGraphViewModel

abstract class SignBaseFragment(@LayoutRes layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val vm: AuthViewModel by sharedGraphViewModel(R.id.auth_graph)

}