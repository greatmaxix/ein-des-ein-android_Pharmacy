package com.pharmacy.components.pharmacy

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.pharmacy.R
import com.pharmacy.components.auth.sign.SignInFragmentArgs
import com.pharmacy.core.base.fragment.dialog.AlertDialogFragment
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.onNavDestinationSelected
import com.pharmacy.core.extensions.showAlert
import kotlinx.android.synthetic.main.fragment_pharmacy.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class PharmacyFragment : BaseMVVMFragment(R.layout.fragment_pharmacy) {

    private val args: PharmacyFragmentArgs by navArgs()

    private val vm: PharmacyViewModel by viewModel { parametersOf(args.productId) }

    private val tabTitles = intArrayOf(R.string.pharmacyListTitle, R.string.pharmacyMapTitle)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        initPager()
    }

    private fun initPager() {
        vpPharmacy.isUserInputEnabled = false
        TabLayoutMediator(tlPharmacy, vpPharmacy.apply {
            adapter = PharmacyPagerAdapter(this@PharmacyFragment)
            offscreenPageLimit = tabTitles.size
        }) { tab, position -> tab.text = getString(tabTitles[position]) }.attach()
    }

    override fun onBindLiveData() {
        observeResult(vm.addProductLiveData) {
            onEmmit = { notifyCardAdded() }
            onError = { errorOrAuth(it.resId) }
        }

        observe(vm.pharmacyLiveData) { pharmacy ->
            pharmacy?.let { navController.navigate(PharmacyFragmentDirections.fromPharmacyToProductInfo(it)) }
        }
    }

    private fun notifyCardAdded() {
        AlertDialogFragment.newInstance(null, getString(R.string.productAddedToCart), getString(R.string.cart), getString(R.string.stay))
            .apply {
                setNegativeListener { dialog, _ -> dialog.dismiss() }
                setPositiveListener { dialog, _ ->
                    requireParentFragment().findNavController().onNavDestinationSelected(R.id.nav_cart, null, R.id.nav_cart, true)
                    dialog.dismiss()
                }
            }.show(childFragmentManager)
    }

    private fun errorOrAuth(@StringRes strResId: Int) {
        if (strResId == R.string.forAddingToCart) {
            showAlert(strResId) {
                cancelable = false
                positive = getString(R.string.signIn)
                negative = getString(R.string.cancel)
                positiveAction = { navController.navigate(R.id.fromPharmacyToAuth, SignInFragmentArgs(R.id.nav_pharmacy).toBundle()) }
            }
        } else {
            messageCallback?.showError(strResId)
        }
    }
}