package com.pulse.components.recipes.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.recipes.model.Recipe
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.databinding.ItemRecipeBinding

class RecipesViewHolder(val itemView: View, private val onClick: Recipe.() -> Unit) : BaseViewHolder<Recipe>(itemView) {

    private val binding by viewBinding(ItemRecipeBinding::bind)

    override fun bind(item: Recipe) = with(binding) {
        with(item) {
            mtvQty.text = getString(R.string.qty, productCount)
            mtvDoctorName.text = doctorName
            mtvDescription.text = productRusName?.wrapHtml
            mtvLabelNumber.text = "№ $code"
            mtvLabelActive.text = getString(R.string.activeUntil, validTill.dateFormatRecipes)
            ivDoctor.loadGlideCircle(doctorUrl, R.drawable.ic_placeholder_drugstore)
            ivProduct.loadGlideProduct(productUrl)
            glContainer.disabled = !status.isActive
            ivRecipeDownload.visibleOrGone(status.isActive)
            ivRecipeDownload.setDebounceOnClickListener { onClick(item) }
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup, onClick: Recipe.() -> Unit) = RecipesViewHolder(parent.inflate(R.layout.item_recipe), onClick)

    }
}