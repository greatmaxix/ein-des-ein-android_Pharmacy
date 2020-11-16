package com.pulse.components.recipes.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.components.recipes.model.Recipe
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import kotlinx.android.synthetic.main.item_recipe.*

class RecipesViewHolder(override val containerView: View, private val onClick: Recipe.() -> Unit) : BaseViewHolder<Recipe>(containerView) {

    override fun bind(item: Recipe) = with(item) {

        mtv_qty.text = stringRes(R.string.qty, productCount)
        mtv_doctor_name.text = doctorName
        mtv_description.text = productRusName?.wrapHtml
        mtv_label_number.text = "№ $code"
        mtv_label_active.text = stringRes(R.string.activeUntil, validTill.dateFormatRecipes)

        iv_doctor.loadGlideCircle(doctorUrl, R.drawable.ic_placeholder_drugstore)
        iv_product.loadGlideProduct(productUrl)

        gl_container.disabled = !status.isActive

        iv_recipe_download.visibleOrGone(status.isActive)
        iv_recipe_download.setDebounceOnClickListener { onClick(item) }
    }

    companion object {

        fun newInstance(parent: ViewGroup, onClick: Recipe.() -> Unit) = RecipesViewHolder(parent.inflate(R.layout.item_recipe), onClick)

    }
}