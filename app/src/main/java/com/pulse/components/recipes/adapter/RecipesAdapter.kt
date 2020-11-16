package com.pulse.components.recipes.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pulse.components.recipes.model.Recipe

class RecipesAdapter(private val onClick: Recipe.() -> Unit) : PagingDataAdapter<Recipe, RecipesViewHolder>(RecipesDiffUtil) {

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecipesViewHolder.newInstance(parent, onClick)

}
