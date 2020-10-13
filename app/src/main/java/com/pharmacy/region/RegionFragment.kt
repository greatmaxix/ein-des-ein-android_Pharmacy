package com.pharmacy.region

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacy.R
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.animateVisibleOrGoneIfNot
import com.pharmacy.core.extensions.falseIfNull
import com.pharmacy.core.extensions.onClick
import com.pharmacy.region.adapter.RegionAdapter
import kotlinx.android.synthetic.main.fragment_region.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class RegionFragment(private val viewModel: RegionViewModel) : BaseMVVMFragment(R.layout.fragment_region) {

    private val regionAdapter = RegionAdapter({
        searchViewRegion.clearFocus()
        viewModel.regionSelected(it)
    }, {
        viewLifecycleOwner.lifecycleScope.launch(Main) {
            llRegionNotFoundContainer.animateVisibleOrGoneIfNot(it)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvRegions.layoutManager = LinearLayoutManager(requireContext())
        rvRegions.adapter = regionAdapter
        attachBackPressCallback { viewModel.handleBackPress() }
        clearFragmentResult(REGION_SELECTION_FINISHED_KEY)
        ivBackRegion.onClick { requireActivity().onBackPressed() }
        searchViewRegion.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                regionAdapter.filter { it.region?.name?.contains(text, true).falseIfNull() || it.header != 0.toChar() }
            }
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.regionsLiveData, regionAdapter::notifyDataSet)
        observe(viewModel.regionSavedLiveData) {
            setFragmentResult(it)
            navigationBack()
        }
    }

    private fun setFragmentResult(value: Boolean) = setFragmentResult(
        com.pharmacy.region.RegionFragment.Companion.REGION_SELECTION_FINISHED_KEY,
        bundleOf(com.pharmacy.region.RegionFragment.Companion.REGION_SELECTION_FINISHED_DATA to value)
    )

    companion object {

        const val REGION_SELECTION_FINISHED_KEY = "REGION_SELECTION_FINISHED"
        const val REGION_SELECTION_FINISHED_DATA = "REGION_SELECTION_FINISHED_DATA"
    }
}