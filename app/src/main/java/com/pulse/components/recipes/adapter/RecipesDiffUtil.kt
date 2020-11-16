package com.pulse.components.recipes.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.components.recipes.model.Recipe

object RecipesDiffUtil : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem

}