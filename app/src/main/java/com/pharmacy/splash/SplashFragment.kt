package com.pharmacy.splash

import android.os.Bundle
import android.view.View
import com.pharmacy.R
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.animateVisible
import kotlinx.android.synthetic.main.fragment_splash.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
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