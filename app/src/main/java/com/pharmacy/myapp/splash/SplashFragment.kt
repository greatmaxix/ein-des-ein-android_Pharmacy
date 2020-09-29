package com.pharmacy.myapp.splash

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.animateVisible
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment(private val viewModel: SplashViewModel) : BaseMVVMFragment(R.layout.fragment_splash) {

    private val duration by lazy { resources.getInteger(R.integer.animation_time).toLong() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateCustomer()
        ivLogo.animateVisible(duration)
    }

    override fun onBindLiveData() {
        observe(viewModel.directionLiveData, navController::navigate)
    }
}