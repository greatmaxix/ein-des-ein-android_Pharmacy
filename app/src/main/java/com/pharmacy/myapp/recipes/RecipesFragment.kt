package com.pharmacy.myapp.recipes

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import kotlinx.android.synthetic.main.fragment_recipes.*

class RecipesFragment : BaseMVVMFragment(R.layout.fragment_recipes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton(R.drawable.ic_arrow_back)
        emptyContentRecipes.setButtonAction {

        }
    }

}