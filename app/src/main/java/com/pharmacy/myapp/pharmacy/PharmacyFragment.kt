package com.pharmacy.myapp.pharmacy

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentArgs
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onNavDestinationSelected
import com.pharmacy.myapp.core.extensions.showAlert
import kotlinx.android.synthetic.main.fragment_pharmacy.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PharmacyFragment : BaseMVVMFragment(R.layout.fragment_pharmacy) {

    private val args: PharmacyFragmentArgs by navArgs()

    private val viewModel: PharmacyViewModel by viewModel { parametersOf(args.productId) }

    private val tabTitles = intArrayOf(R.string.pharmacyListTitle, R.string.pharmacyMapTitle)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        initPager()
    }

    override fun onBindLiveData() {
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.errorLiveData, ::errorOrAuth)
    }

    private fun errorOrAuth(@StringRes strResId: Int) {
        if (strResId == R.string.forAddingToCart) {
            showAlert(strResId) {
                cancelable = false
                positive = getString(R.string.signIn)
                negative = getString(R.string.cancel)
                positiveAction = { navController.navigate(R.id.fromPharmacyToAuth, SignInFragmentArgs(R.id.nav_pharmacy).toBundle()) }
                negativeAction = { navController.onNavDestinationSelected(R.id.nav_home, inclusive = true) }
            }
        } else {
            messageCallback?.showError(strResId)
        }
    }

    private fun initPager() {
        vpPharmacy.isUserInputEnabled = false
        TabLayoutMediator(tlPharmacy, vpPharmacy.apply {
            adapter = PharmacyPagerAdapter(this@PharmacyFragment)
            offscreenPageLimit = tabTitles.size
        }) { tab, position -> tab.text = getString(tabTitles[position]) }.attach()
    }
}