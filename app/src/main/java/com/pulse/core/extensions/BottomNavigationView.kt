package com.pulse.core.extensions

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pulse.R

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