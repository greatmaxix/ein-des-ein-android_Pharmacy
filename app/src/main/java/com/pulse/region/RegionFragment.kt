package com.pulse.region

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.animateVisibleOrGoneIfNot
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.extensions.notifySavedStateHandle
import com.pulse.core.extensions.onClick
import com.pulse.region.adapter.RegionAdapter
import kotlinx.android.synthetic.main.fragment_region.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class RegionFragment(private val viewModel: RegionViewModel) : BaseMVVMFragment(R.layout.fragment_region) {

    private val regionAdapter by lazy {
        RegionAdapter({
            searchViewRegion.clearFocus()
            viewModel.regionSelected(it)
        }, {
            viewLifecycleOwner.lifecycleScope.launch { llRegionNotFoundContainer.animateVisibleOrGoneIfNot(it) }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvRegions.adapter = regionAdapter

        ivBackRegion.onClick { requireActivity().onBackPressed() }

        searchViewRegion.setSearchListener { text ->
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