package com.pulse.components.region

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.region.adapter.RegionAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentRegionBinding
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class RegionFragment : BaseMVVMFragment<RegionViewModel>(R.layout.fragment_region, RegionViewModel::class) {

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

    override fun initUI() = with(binding) {
        rvRegions.adapter = regionAdapter
        ivBack.setDebounceOnClickListener { requireActivity().onBackPressed() }
        viewSearch.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                regionAdapter.filter { it.region?.name?.contains(text, true).falseIfNull() || it.header != 0.toChar() }
            }
        }

        attachBackPressCallback { viewModel.handleBackPress() }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.regionsState, regionAdapter::notifyDataSet)
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.regionSavedEvent.events) {
            notifySavedStateHandle(REGION_KEY, it)
            navigationBack()
        }
    }

    companion object {

        const val REGION_KEY = "regionKey"
    }
}