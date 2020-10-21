package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.pulse.R
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.visible
import com.pulse.model.category.Category
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_category_home.view.*

class CategoryHomeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_category_home, true)

    init {
        setBackgroundResource(R.drawable.background_default_ripple_selector_rect)
        orientation = VERTICAL
    }

    fun setCategory(category: Category) {
        tvCategoryHome.text = category.name
        tvCategoryHome.visible()
        ivCategoryHome.visible()
    }

}