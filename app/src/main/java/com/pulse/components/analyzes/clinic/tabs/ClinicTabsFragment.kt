package com.pulse.components.analyzes.clinic.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.pulse.R
import com.pulse.components.analyzes.clinic.tabs.ClinicTabsFragmentDirections.Companion.fromClinicMapToClinicBottomSheet
import com.pulse.components.analyzes.clinic.tabs.ClinicTabsFragmentDirections.Companion.globalToAnalyzeCategories
import com.pulse.components.analyzes.clinic.tabs.ClinicTabsFragmentDirections.Companion.globalToAnalyzeCheckout
import com.pulse.components.analyzes.clinic.tabs.adapter.ClinicTabsPagerAdapter
import com.pulse.components.analyzes.clinic.tabs.map.ClinicMapBottomSheet.Companion.KEY_CLINIC_MAP_BOTTOM_SHEET
import com.pulse.components.analyzes.clinic.tabs.map.ClinicMapBottomSheet.Companion.KEY_ORDER_SERVICE
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.databinding.FragmentClinicTabsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ClinicTabsFragment : BaseMVVMFragment(R.layout.fragment_clinic_tabs) {

    private val args by navArgs<ClinicTabsFragmentArgs>()
    private val binding by viewBinding(FragmentClinicTabsBinding::bind)
    private val viewModel: ClinicTabsViewModel by viewModel()
    private val tabTitles by lazy { listOf(getString(R.string.pharmacyListTitle), getString(R.string.pharmacyMapTitle)) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        initPager()

        setFragmentResultListener(KEY_CLINIC_MAP_BOTTOM_SHEET) { _, bundle ->
            val clinic = bundle.getParcelable<Clinic>(KEY_ORDER_SERVICE)
            clinic?.let(viewModel::orderService)
        }
    }

    private fun initPager() = with(binding) {
        vpClinics.isUserInputEnabled = false
        TabLayoutMediator(tabsClinics, vpClinics.apply {
            adapter = ClinicTabsPagerAdapter(this@ClinicTabsFragment)
            offscreenPageLimit = tabTitles.size
        }) { tab, position -> tab.text = tabTitles[position] }.attach()
    }

    override fun onBindLiveData() {
        observe(viewModel.clinicLiveData) { clinic ->
            clinic?.let { navController.navigate(fromClinicMapToClinicBottomSheet(it)) }
        }
        observe(viewModel.selectedClinicLiveData) { clinic ->
            navController.navigate(
                if (args.category == null) {
                    globalToAnalyzeCategories(clinic)
                } else {
                    globalToAnalyzeCheckout(args.category!!, clinic)
                }
            )
        }
    }
}