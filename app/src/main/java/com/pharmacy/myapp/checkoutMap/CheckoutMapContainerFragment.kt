package com.pharmacy.myapp.checkoutMap

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.falseIfNull
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import com.pharmacy.myapp.ui.ZoomOutPageTransformer
import kotlinx.android.synthetic.main.fragment_checkout_map_container.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckoutMapContainerFragment : BaseMVVMFragment(R.layout.fragment_checkout_map_container) {

    private val viewModel: CheckoutMapViewModel by sharedGraphViewModel(R.id.checkout_map_graph)

    private val tabTitles = intArrayOf(R.string.checkoutListTitle, R.string.checkoutMapTitle)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        initPager()

        lifecycleScope.launch { // todo temporary
            delay(2000)
            if (arguments?.getBoolean("showMap").falseIfNull()) { vpCheckoutMap.currentItem = 1 }
        }
    }

    override fun onBindLiveData() {
        viewModel.selectedDrugstoreLiveData.observeExt {
            if (vpCheckoutMap.currentItem == 0) vpCheckoutMap.currentItem = 1
        }
        viewModel.directionLiveData.observeExt(navController::navigate)
    }

    private fun initPager() {
        vpCheckoutMap.isUserInputEnabled = false
        TabLayoutMediator(tlCheckoutMap, vpCheckoutMap.apply {
            adapter = CheckoutMapPagerAdapter(this@CheckoutMapContainerFragment)
            offscreenPageLimit = tabTitles.size
            setPageTransformer(ZoomOutPageTransformer())
        }) { tab, position -> tab.text = getString(tabTitles[position]) }.attach()
    }

}