package com.pharmacy.myapp.home

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.debug
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.home.HomeFragmentDirections.Companion.globalToDevTools
import com.pharmacy.myapp.home.HomeFragmentDirections.Companion.globalToQrCodeScanner
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseMVVMFragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mcvScanHome.onClick {
            navController.navigate(globalToQrCodeScanner())
        }
        mcvAskHome.onClick { }
        mcvMapHome.onClick { }
        mcvAnalyzeHome.onClick { }
        mcvOrderContainer.onClick { }
        mcvSearchHome.onClick { }

        // Developers screen for convenient features access
        debug {
            mcvToolbarHome.setOnLongClickListener {
                navController.navigate(globalToDevTools())
                true
            }
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.errorLiveData.observeExt { messageCallback?.showError(it) }
        viewModel.progressLiveData.observeExt { progressCallback?.setInProgress(it) }
    }
}