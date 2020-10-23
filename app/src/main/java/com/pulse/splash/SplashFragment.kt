package com.pulse.splash

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SplashFragment(private val viewModel: SplashViewModel) : BaseMVVMFragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateCustomer()
    }

    override fun onBindLiveData() {
        observe(viewModel.directionLiveData, navController::navigate)
    }
}