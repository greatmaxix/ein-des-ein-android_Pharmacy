package com.pharmacy.myapp.checkoutMap

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.ui.ZoomOutPageTransformer
import kotlinx.android.synthetic.main.fragment_checkout_map_container.*

class CheckoutMapContainerFragment : BaseMVVMFragment(R.layout.fragment_checkout_map_container) {

    private val tabTitles = intArrayOf(R.string.checkoutListTitle, R.string.checkoutMapTitle)
    private val pagerAdapter by lazy { CheckoutMapPagerAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        TabLayoutMediator(tlCheckoutMap, vpCheckoutMap.apply {
            adapter = pagerAdapter
            offscreenPageLimit = tabTitles.size
            setPageTransformer(ZoomOutPageTransformer())
        }) { tab, position -> tab.text = getString(tabTitles[position]) }.attach()
    }


}