package com.pulse.components.recipes

import android.os.Bundle
import android.view.View
import androidx.paging.PagingData
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.recipes.adapter.RecipesAdapter
import com.pulse.components.recipes.model.Recipe
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.addStateListener
import com.pulse.core.extensions.onNavDestinationSelected
import com.pulse.core.extensions.showAlertRes
import com.pulse.core.extensions.visibleOrGone
import com.pulse.data.GeneralException
import kotlinx.android.synthetic.main.fragment_recipes.*

class RecipesFragment(private val vm: RecipesViewModel) : BaseMVVMFragment(R.layout.fragment_recipes) {

    private val recipesAdapter = RecipesAdapter {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        rvRecipes.adapter = recipesAdapter

        recipesAdapter.addStateListener { progressCallback?.setInProgress(it) }
    }

    override fun onBindLiveData() {
        observe(vm.recipesLiveData, ::showRecipesOrContainer)
        observe(vm.recipesErrorLiveData) { it.contentOrNull?.let(::showAlert) }
        observe(vm.recipesCountLiveData) { emptyContentRecipes.visibleOrGone(it <= 0) }
    }

    private fun showRecipesOrContainer(pagingData: PagingData<Recipe>) {
        recipesAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
    }

    private fun showAlert(generalException: GeneralException) {
        if (generalException.resId == R.string.toSeeYourRecipes) {
            showAlertRes(getString(R.string.toSeeYourRecipes)) {
                cancelable = false
                positive = R.string.signIn
                negative = R.string.cancel

                positiveAction = { navController.navigate(R.id.fromRecipesToAuth, SignInFragmentArgs(R.id.nav_recipes).toBundle()) }
                negativeAction = { navController.onNavDestinationSelected(R.id.nav_home, inclusive = true) }
            }
        }
    }
}