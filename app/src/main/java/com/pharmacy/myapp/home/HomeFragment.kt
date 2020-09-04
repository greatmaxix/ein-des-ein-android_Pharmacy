package com.pharmacy.myapp.home

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToChat
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToCheckoutMap
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.debug
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.onNavDestinationSelected
import com.pharmacy.myapp.home.HomeFragmentDirections.Companion.fromHomeToScanner
import com.pharmacy.myapp.home.HomeFragmentDirections.Companion.globalToDevTools
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseMVVMFragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mcvScanHome.onClick { doNav(fromHomeToScanner()) }
        mcvAskHome.onClick { doNav(globalToChat()) }
        mcvAnalyzeHome.onClick { navController.onNavDestinationSelected(R.id.nav_analyzes, null, R.id.nav_home) }
        uploadRecipes.onClick { navController.onNavDestinationSelected(R.id.nav_recipes, null, R.id.nav_home) }
        mcvSearchHome.onClick { navController.onNavDestinationSelected(R.id.nav_search, null, R.id.nav_home) }
        mcvMapHome.onClick { navController.navigate(globalToCheckoutMap(true)) }
        mcvOrderContainer.onClick { }

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