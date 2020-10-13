package com.pharmacy.auth.sign

import androidx.annotation.LayoutRes
import com.pharmacy.R
import com.pharmacy.auth.AuthViewModel
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.sharedGraphViewModel

abstract class SignBaseFragment(@LayoutRes layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val vm: AuthViewModel by sharedGraphViewModel(R.id.auth_graph)

}