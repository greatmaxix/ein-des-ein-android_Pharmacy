package com.pharmacy.myapp.utils.extension

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.toggle

fun BottomNavigationView.hideNav(duration: Long = resources.getInteger(R.integer.animation_time).toLong()) {
    if (translationY == 0f) {
        toggle(true, duration)
    }
}

fun BottomNavigationView.showNav(duration: Long = resources.getInteger(R.integer.animation_time).toLong()) {
    if (translationY > 0f) {
        toggle(true, duration)
    }
}