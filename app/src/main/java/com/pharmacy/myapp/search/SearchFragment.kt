package com.pharmacy.myapp.search

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment(private val viewModel: SearchViewModel) : BaseMVVMFragment(R.layout.fragment_search) {

    private val searchHistoryAdapter = SearchHistoryAdapter() {
        searchView.setText(it)
    }.apply {
        setList(mutableListOf("Дротаверин", "Анальгин"))// todo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mcvScanSearch.onClick { navController.onNavDestinationSelected(R.id.globalToQrCodeScanner, null, R.id.nav_search) }
        searchView.setSearchListener { viewModel.doSearch(it.toString()) }

        rvOrdersListOrder.setHasFixedSize(true)
        rvOrdersListOrder.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvOrdersListOrder.adapter = searchHistoryAdapter
        tvClearSearchHistory.onClick {
            searchHistoryAdapter.setList(mutableListOf())
            tvClearSearchHistory.gone() // todo mock
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.oftenLookingLiveData.observeExt {
            it.forEach { item -> asyncWithContext({ createChip(item) }, { cgOftenLookingForSearch.addView(this) }) }
        }
        viewModel.searchLiveData.observeExt {
            llDrugsNotFoundContainer.animateVisibleOrGoneIfNot(it.isNotEmpty())// todo mock
            clSearchContent.animateVisibleOrGoneIfNot(it.isEmpty())
        }
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
        onClick { searchView.setText(text) }
    }

}