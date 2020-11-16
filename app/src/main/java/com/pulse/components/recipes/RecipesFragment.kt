package com.pulse.components.recipes

import android.os.Bundle
import android.view.View
import androidx.paging.PagingData
import com.pulse.R
import com.pulse.components.recipes.adapter.RecipesAdapter
import com.pulse.components.recipes.model.Recipe
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.addStateListener
import com.pulse.core.extensions.visibleOrGone
import kotlinx.android.synthetic.main.fragment_recipes.*
import timber.log.Timber

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
        observe(vm.recipesCountLiveData) {
            Timber.e("It${it}")
            emptyContentRecipes.visibleOrGone(it <= 0)
        }
    }

    private fun showRecipesOrContainer(pagingData: PagingData<Recipe>) {
        recipesAdapter.submitData(lifecycle, pagingData)
    }
}