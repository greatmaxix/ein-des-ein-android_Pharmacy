package com.pharmacy.myapp.auth

import androidx.annotation.LayoutRes
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel

abstract class AuthBaseFragment(@LayoutRes layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val viewModel: AuthViewModel by sharedGraphViewModel(R.id.auth_graph)

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.directionPopBackLiveData) { navController.popBackStack(it, false) }
    }
}