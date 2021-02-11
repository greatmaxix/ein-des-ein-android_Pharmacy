package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.pulse.core.extensions.animateVisible
import com.pulse.core.extensions.inflater
import com.pulse.core.extensions.loadGlideDrawableByName
import com.pulse.databinding.ViewHomeCategoryBinding

class CategoryHomeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewHomeCategoryBinding.inflate(inflater, this)

    fun setCategory(pair: Pair<String?, Int>) = with(binding) {
        val (text, index) = pair
        mtvCategory.text = text
        ivCategory.loadGlideDrawableByName("ic_category_${index + 1}") {
            it.animateVisible()
            mtvCategory.animateVisible()
        }
    }
}