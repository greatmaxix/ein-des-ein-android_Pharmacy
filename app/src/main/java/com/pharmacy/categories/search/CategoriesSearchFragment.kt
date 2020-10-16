package com.pharmacy.categories.search

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pharmacy.R
import com.pharmacy.components.auth.sign.SignInFragmentArgs
import com.pharmacy.core.extensions.spanSearchCount
import com.pharmacy.core.extensions.visibleOrGone
import com.pharmacy.productList.BaseProductListFragment
import kotlinx.android.synthetic.main.fragment_categories_search.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
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