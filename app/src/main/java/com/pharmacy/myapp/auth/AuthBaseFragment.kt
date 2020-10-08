package com.pharmacy.myapp.auth

import androidx.annotation.LayoutRes
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel

abstract class AuthBaseFragment(@LayoutRes layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val vm: AuthViewModel by sharedGraphViewModel(R.id.auth_graph)

    override fun onBindLiveData() {
        observe(vm.directionLiveData, navController::navigate)
        observe(vm.directionPopBackLiveData) { navController.popBackStack(it, false) }
    }
}