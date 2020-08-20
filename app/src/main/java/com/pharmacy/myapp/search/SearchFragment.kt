package com.pharmacy.myapp.search

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addDrawableItemDivider
import com.pharmacy.myapp.search.adapter.SearchAdapter
import com.pharmacy.myapp.search.extra.ISearchCallback
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchFragment(private val viewModel: SearchViewModel) : BaseMVVMFragment(R.layout.fragment_search), ISearchCallback {

    private val searchAdapter = SearchAdapter {
        Timber.e("Name: ${it?.rusName}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvProducts.adapter = searchAdapter
        rvProducts.addDrawableItemDivider(R.drawable.divider_search_)
    }

    override fun onBindLiveData() {
        observe(viewModel.pagedSearchLiveData) {
            viewLifecycleOwner.lifecycleScope.launch {
                searchAdapter.submitData(it)
            }
        }

        observe(viewModel.productCountLiveData) {
            tvSearchResult.text = getString(R.string.countProducts, it)
        }
    }

    override fun doSearch(text: String) = viewModel.doSearch(text)

}