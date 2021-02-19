package com.pulse.components.analyzes.clinic

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.category.adapter.AnalyzeCategoryAdapter
import com.pulse.components.analyzes.details.AnalyzeDetailsFragmentDirections
import com.pulse.components.analyzes.details.adapter.ClinicsAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.loadGlideDrawableByURL
import com.pulse.core.extensions.showDial
import com.pulse.core.extensions.showDirection
import com.pulse.databinding.FragmentClinicCardBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ClinicCardFragment(private val viewModel: ClinicCardViewModel) : BaseMVVMFragment(R.layout.fragment_clinic_card) {

    private val args by navArgs<ClinicCardFragmentArgs>()
    private val binding by viewBinding(FragmentClinicCardBinding::bind)
    private val categoryAdapter by lazy { AnalyzeCategoryAdapter(viewModel::selectCategory) }
    private val clinicsAdapter = ClinicsAdapter(
        { navController.navigate(AnalyzeDetailsFragmentDirections.fromAnalyzeDetailsToClinicCard(it)) },
        null,
        ::showDial,
        { showDirection(it.location.lat, it.location.lng) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

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
        }
    }

    override fun onBindLiveData() {
        observeResult(viewModel.branchesListLiveData) {
            onEmmit = {
                binding.mtvBranch.text = getString(R.string.branch_holder, this.size)
                clinicsAdapter.notifyItems(this)
            }
        }
        observeResult(viewModel.categoriesLiveData) {
            onEmmit = {
                categoryAdapter.notifyDataSet(this)
            }
        }
    }
}