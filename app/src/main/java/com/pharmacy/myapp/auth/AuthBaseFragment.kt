package com.pharmacy.myapp.auth

import androidx.annotation.LayoutRes
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel

abstract class AuthBaseFragment(@LayoutRes layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val viewModel: AuthViewModel by sharedGraphViewModel(R.id.auth_graph)

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.directionLiveData.observeExt(navController::navigate)
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
    }

}