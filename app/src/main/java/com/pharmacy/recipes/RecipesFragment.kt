package com.pharmacy.recipes

import android.os.Bundle
import android.view.View
import com.pharmacy.R
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import kotlinx.android.synthetic.main.fragment_recipes.*

class RecipesFragment : BaseMVVMFragment(R.layout.fragment_recipes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        emptyContentRecipes.setButtonAction {

        }
    }

}