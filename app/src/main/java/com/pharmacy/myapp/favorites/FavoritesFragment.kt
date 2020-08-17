package com.pharmacy.myapp.favorites

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onNavDestinationSelected
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : BaseMVVMFragment(R.layout.fragment_favorites) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton(R.drawable.ic_arrow_back)
        emptyContentFavorites.setButtonAction {
            navController.onNavDestinationSelected(R.id.search_graph, null, R.id.nav_home)
        }
    }

}