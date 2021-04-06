package com.pulse.components.pharmacy

import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.pharmacy.adapter.PharmacyPagerAdapter
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.base.fragment.dialog.AlertDialogFragment
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.onNavDestinationSelected
import com.pulse.databinding.FragmentPharmacyBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class PharmacyFragment : BaseToolbarFragment<PharmacyViewModel>(R.layout.fragment_pharmacy, PharmacyViewModel::class) {

    private val args: PharmacyFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentPharmacyBinding::bind)
    override val viewModel: PharmacyViewModel by viewModel { parametersOf(args.productId) }
    private val tabTitles = intArrayOf(R.string.pharmacyListTitle, R.string.pharmacyMapTitle)

    override fun initUI() {
        showBackButton()
        initPager()
    }

    private fun initPager() = with(binding) {
        vpPharmacy.isUserInputEnabled = false
        TabLayoutMediator(tabsPharmacy, vpPharmacy.apply {
            adapter = PharmacyPagerAdapter(this@PharmacyFragment)
            offscreenPageLimit = tabTitles.size
        }) { tab, position -> tab.text = getString(tabTitles[position]) }.attach()
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.pharmacyEvent.events) { pharmacy ->
            pharmacy?.let { navController.navigate(PharmacyFragmentDirections.fromPharmacyToProductInfo(it)) }
        }
        observe(viewModel.errorEvent.events) { errorOrAuth(it.resId) }
        observe(viewModel.addProductResultEvent.events) { notifyCardAdded() }
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
            uiHelper.showDialog(getString(strResId)) {
                cancelable = false
                positive = getString(R.string.signIn)
                negative = getString(R.string.cancel)
                positiveAction = { navController.navigate(R.id.fromPharmacyToAuth, SignInFragmentArgs(R.id.nav_pharmacy).toBundle()) }
            }
        } else {
            uiHelper.showMessage(getString(strResId))
        }
    }
}