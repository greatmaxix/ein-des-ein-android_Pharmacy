package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.pulse.R
import com.pulse.core.extensions.animateVisible
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.loadGlideDrawableByName
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_home_category.view.*

class CategoryHomeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr),
    LayoutContainer {

    override val containerView = inflate(R.layout.view_home_category, true)

    fun setCategory(pair: Pair<String?, Int>) {
        val (text, index) = pair
        tvCategoryHome.text = text
        ivCategoryHome.loadGlideDrawableByName("ic_category_${index + 1}") {
            it.animateVisible()
            tvCategoryHome.animateVisible()
        }
    }
}