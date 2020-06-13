package com.pharmacy.myapp.ui

import android.content.Context
import android.util.AttributeSet
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.view.LoaderView

class LoaderViewImpl @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LoaderView(context, attrs, defStyleAttr, defStyleRes) {

    override fun getLayoutLoaderId(): Int = R.layout.layout_loader

    override fun configViews() {}

    override fun performStart() {}

    override fun performStop() {}
}