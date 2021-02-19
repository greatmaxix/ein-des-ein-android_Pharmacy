package com.pulse.components.analyzes.details

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.details.AnalyzeDetailsFragmentDirections.Companion.fromAnalyzeDetailsToAnalyzeCheckout
import com.pulse.components.analyzes.details.AnalyzeDetailsFragmentDirections.Companion.fromAnalyzeDetailsToClinicCard
import com.pulse.components.analyzes.details.adapter.ClinicsAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.addItemDecorator
import com.pulse.core.extensions.showDial
import com.pulse.core.extensions.showDirection
import com.pulse.databinding.FragmentAnalyzeDetailsBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzeDetailsFragment(private val viewModel: AnalyzeDetailsViewModel) : BaseMVVMFragment(R.layout.fragment_analyze_details) {

    private val args by navArgs<AnalyzeDetailsFragmentArgs>()
    private val binding by viewBinding(FragmentAnalyzeDetailsBinding::bind)
    private val clinicsAdapter = ClinicsAdapter(
        { navController.navigate(fromAnalyzeDetailsToClinicCard(it)) },
        { navController.navigate(fromAnalyzeDetailsToAnalyzeCheckout(args.category, it)) },
        ::showDial,
        { showDirection(it.location.lat, it.location.lng) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        val category = args.category
        category.code?.let(viewModel::requestClinics)
        toolbar.toolbar.title = category.name
        mtvAnalyzeCategory.text = category.name
        mtvCategoryCode.text = category.code
        mtvDescription.text = category.description
        rvClinics.adapter = clinicsAdapter
        rvClinics.addItemDecorator()
    }

    override fun onBindLiveData() {
        observeResult(viewModel.clinicsListLiveData) {
            onEmmit = {
                binding.mtvClinics.text = getString(R.string.clinics_holder, this.size)
                clinicsAdapter.notifyItems(this)
            }
        }
    }
}