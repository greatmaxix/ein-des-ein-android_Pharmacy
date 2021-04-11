package com.pulse.components.analyzes.details

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.details.AnalyzeDetailsFragmentDirections.Companion.globalToAnalyzeCheckout
import com.pulse.components.analyzes.details.AnalyzeDetailsFragmentDirections.Companion.globalToClinicCard
import com.pulse.components.analyzes.details.AnalyzeDetailsFragmentDirections.Companion.globalToClinicTabs
import com.pulse.components.analyzes.details.adapter.ClinicsAdapter
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentAnalyzeDetailsBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzeDetailsFragment :
    BaseToolbarFragment<AnalyzeDetailsViewModel>(R.layout.fragment_analyze_details, AnalyzeDetailsViewModel::class) {

    private val args by navArgs<AnalyzeDetailsFragmentArgs>()
    private val binding by viewBinding(FragmentAnalyzeDetailsBinding::bind)
    private val clinicsAdapter = ClinicsAdapter(
        { navController.navigate(globalToClinicCard(it)) },
        { navController.navigate(globalToAnalyzeCheckout(args.category, it)) },
        ::showDial,
        { showDirection(it.location.lat, it.location.lng) }
    )

    override fun initUI() = with(binding) {
        showBackButton()
        val category = args.category
        val clinic = args.clinic
        category.code?.let(viewModel::requestClinics)
        toolbar.toolbar.title = category.name
        mtvAnalyzeCategory.text = category.name
        mtvCategoryCode.text = category.code
        mtvDescription.text = category.description
        if (clinic != null) {
            groupClinics.gone()
            mbOrderService.visible()
            mbOrderService.onClickDebounce { navController.navigate(globalToAnalyzeCheckout(category, clinic)) }
        } else {
            mbOrderService.gone()
            rvClinics.adapter = clinicsAdapter
            rvClinics.addItemDecorator()
            mbSeeAll.onClickDebounce { navController.navigate(globalToClinicTabs()) }
            groupClinics.visible()
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.clinicsListState) {
            binding.mtvClinics.text = getString(R.string.clinics_holder, it.size)
            clinicsAdapter.notifyItems(it)
        }
    }
}