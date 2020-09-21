package com.pharmacy.myapp.categories.search

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentArgs
import com.pharmacy.myapp.core.extensions.spanSearchCount
import com.pharmacy.myapp.core.extensions.visibleOrGone
import com.pharmacy.myapp.produtcList.BaseProductListFragment
import kotlinx.android.synthetic.main.fragment_search.*

class CategoriesSearchFragment(private val viewModel: CategoriesSearchViewModel) :
    BaseProductListFragment<CategoriesSearchViewModel>(R.layout.fragment_categories_search, viewModel) {

    private val args by navArgs<CategoriesSearchFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        toolbar?.title = args.category.name

        viewModel.doCategorySearch(args.category.code)
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.productCountLiveData) {
            tvSearchResult.text = getString(R.string.countProducts, it).spanSearchCount(it)
            llDrugsNotFoundContainer.visibleOrGone(it == 0)
        }
    }

    override fun needToLogin() = navController.navigate(R.id.fromSearchToAuth, SignInFragmentArgs(R.id.nav_search).toBundle())

    override fun notifyWish(globalProductId: Int) = productAdapter.notifyWish(globalProductId)

    override val productLiveData
        get() = viewModel.pagedSearchLiveData
}