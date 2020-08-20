package com.pharmacy.myapp.quickAccess

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.quickAccess.adapter.QuickAccessAdapter
import com.pharmacy.myapp.search.SearchFragment
import kotlinx.android.synthetic.main.fragment_quick_access.*
import kotlinx.coroutines.launch

class QuickAccessFragment(private val viewModel: QuickAccessViewModel, private val searchFragment: SearchFragment) : BaseMVVMFragment(R.layout.fragment_quick_access) {

    private val accessAdapter by lazy { QuickAccessAdapter(::setTextAndSearch) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mcvScanSearch.onClick { navController.onNavDestinationSelected(R.id.globalToQrCodeScanner, null, R.id.nav_quick) }

        ivBack.onClick { showOrHideSearch(false) }

        with(rvQuickAccess) {
            setHasFixedSize(true)
            addAutoKeyboardCloser()
            adapter = accessAdapter
        }

        tvClear.onClick {
            showAlertRes(getString(R.string.previousRequest)) {
                title = R.string.clearSearchHistory
                positive = R.string.clear
                positiveAction = {
                    viewModel.clearQuickAccess()
                    tvClear.gone()
                }
                negative = R.string.cancel
            }
        }

        searchView.setEditorListener {
            showSearchResult(it)
            false
        }

        addBackPressListener {
            if (ivBack.isVisible) showOrHideSearch(false) else navController.navigate(R.id.home_graph)
        }

        setFragmentIfNeed(searchFragment, R.id.fcvSearch)
    }

    override fun onBindLiveData() {
        observe(viewModel.oftenLiveData) {
            it.forEach { item -> asyncWithContext({ createChip(item) }, cgOftenLooking::addView) }
        }

        observe(viewModel.quickAccessLiveData) {
            tvClear.visibleOrGone(it.isNotEmpty())
            viewLifecycleOwner.lifecycleScope.launch { accessAdapter.notifyItems(it) }
        }
    }

    private fun setTextAndSearch(text: String) {
        searchView.setTextAndOpen(text)
        showSearchResult(text)
    }

    private fun showSearchResult(text: String) {
        searchFragment.doSearch(viewModel.addQuickAccess(text))
        showOrHideSearch(true)
    }

    private fun showOrHideSearch(isVisible: Boolean) {
        fcvSearch.animateVisibleOrGoneIfNot(isVisible)
        ivBack.animateVisibleOrGoneIfNot(isVisible)
    }

    private fun createChip(text: String) = Chip(context).apply {
        setTextColor(colorCompat(R.color.primaryBlue))
        setChipBackgroundColorResource(R.color.colorSearchChipBackground)
        setRippleColorResource(R.color.primaryBlueRipple)
        setEnsureMinTouchTargetSize(false)
        setText(text)
        val radius = resources.getDimension(R.dimen._4sdp)
        shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCornerSizes(radius).build()
        chipEndPadding = resources.getDimension(R.dimen._2sdp)
        chipStartPadding = resources.getDimension(R.dimen._2sdp)
        onClick { setTextAndSearch(text) }
    }
}