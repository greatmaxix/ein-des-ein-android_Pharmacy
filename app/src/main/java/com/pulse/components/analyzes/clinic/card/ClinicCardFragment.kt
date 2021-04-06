package com.pulse.components.analyzes.clinic.card

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.category.adapter.AnalyzeCategoryAdapter
import com.pulse.components.analyzes.clinic.card.ClinicCardFragmentDirections.Companion.globalToAnalyzeCategories
import com.pulse.components.analyzes.clinic.card.ClinicCardFragmentDirections.Companion.globalToClinicCard
import com.pulse.components.analyzes.clinic.card.ClinicCardFragmentDirections.Companion.globalToClinicTabs
import com.pulse.components.analyzes.details.adapter.ClinicsAdapter
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentClinicCardBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ClinicCardFragment : BaseToolbarFragment<ClinicCardViewModel>(R.layout.fragment_clinic_card, ClinicCardViewModel::class) {

    private val args by navArgs<ClinicCardFragmentArgs>()
    private val binding by viewBinding(FragmentClinicCardBinding::bind)
    private val categoryAdapter by lazy { AnalyzeCategoryAdapter { navController.navigate(globalToAnalyzeCategories(args.clinic, it)) } }
    private val clinicsAdapter = ClinicsAdapter(
        { navController.navigate(globalToClinicCard(it)) },
        null,
        ::showDial,
        { showDirection(it.location.lat, it.location.lng) }
    )

    override fun initUI() = with(binding) {
        showBackButton()
        with(args.clinic) {
            toolbar.toolbar.title = name
            rvClinics.adapter = clinicsAdapter
            rvServices.adapter = categoryAdapter
            ivImage.loadGlideDrawableByURL(logo.url)
            mtvName.text = getString(R.string.clinic_name_holder, name, rating)
            mtvDescription.text = description
            viewModel.fetchCategories(id)
            viewModel.fetchBranches(id)
            mbSeeAllServices.onClickDebounce { navController.navigate(globalToAnalyzeCategories()) }
            mbSeeAllBranch.onClickDebounce { navController.navigate(globalToClinicTabs()) }
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.branchesListState) {
            binding.mtvBranch.text = getString(R.string.branch_holder, it.size)
            clinicsAdapter.notifyItems(it)
        }
        observe(viewModel.categoriesListState, categoryAdapter::notifyDataSet)
    }
}