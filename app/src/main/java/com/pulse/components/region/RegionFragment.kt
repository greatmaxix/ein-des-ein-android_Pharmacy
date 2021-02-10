package com.pulse.components.region

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.region.adapter.RegionAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.animateVisibleOrGoneIfNot
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.extensions.notifySavedStateHandle
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.FragmentRegionBinding
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class RegionFragment(private val viewModel: RegionViewModel) : BaseMVVMFragment(R.layout.fragment_region) {

    private val binding by viewBinding(FragmentRegionBinding::bind)
    private val regionAdapter by lazy {
        RegionAdapter(
            {
                binding.viewSearch.clearFocus()
                viewModel.regionSelected(it)
            },
            { value -> viewLifecycleOwner.lifecycleScope.launch { binding.llRegionNotFoundContainer.animateVisibleOrGoneIfNot(value) } }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        rvRegions.adapter = regionAdapter
        ivBack.setDebounceOnClickListener { requireActivity().onBackPressed() }
        viewSearch.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                regionAdapter.filter { it.region?.name?.contains(text, true).falseIfNull() || it.header != 0.toChar() }
            }
        }

        attachBackPressCallback { viewModel.handleBackPress() }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.regionsLiveData, regionAdapter::notifyDataSet)
        observe(viewModel.regionSavedLiveData) {
            notifySavedStateHandle(REGION_KEY, it)
            navigationBack()
        }
    }

    companion object {

        const val REGION_KEY = "regionKey"
    }
}