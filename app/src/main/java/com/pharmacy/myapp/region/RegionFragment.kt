package com.pharmacy.myapp.region

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.animateVisibleOrGoneIfNot
import com.pharmacy.myapp.core.extensions.falseIfNull
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.model.region.RegionWithHeader
import com.pharmacy.myapp.region.adapter.RegionAdapter
import kotlinx.android.synthetic.main.fragment_region.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class RegionFragment(private val viewModel: RegionViewModel) : BaseMVVMFragment(R.layout.fragment_region) {

    private val regionAdapter = RegionAdapter({
        searchViewRegion.clearFocus()
        viewModel.regionSelected(it)
    }, {
        lifecycleScope.launch(Main) {
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
            lifecycleScope.launch {
                regionAdapter.filter { it.region?.name?.contains(text, true).falseIfNull() || it.header != 0.toChar() }
            }
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData)  { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.regionsLiveData, regionAdapter::notifyDataSet)
        observe(viewModel.regionSavedLiveData) {
            setFragmentResult(it)
            navigationBack()
        }
    }

    private fun setFragmentResult(value: Boolean) = setFragmentResult(REGION_SELECTION_FINISHED_KEY, bundleOf(REGION_SELECTION_FINISHED_DATA to value))

    companion object {

        const val REGION_SELECTION_FINISHED_KEY = "REGION_SELECTION_FINISHED"
        const val REGION_SELECTION_FINISHED_DATA = "REGION_SELECTION_FINISHED_DATA"
    }
}