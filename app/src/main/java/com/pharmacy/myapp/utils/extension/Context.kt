package com.pharmacy.myapp.utils.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes

fun Context.toolbarHeight(): Int {
    val styledAttributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
    val barSize = styledAttributes.getDimension(0, 0f).toInt()
    styledAttributes.recycle()
    return barSize
}

fun Context.browseUrl(@StringRes url: Int) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(url)))
    if (intent.resolveActivity(packageManager) != null) startActivity(intent)
}
