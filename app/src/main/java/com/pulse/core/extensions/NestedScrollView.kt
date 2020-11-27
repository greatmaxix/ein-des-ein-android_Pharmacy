package com.pulse.core.extensions

import androidx.core.widget.NestedScrollView

fun NestedScrollView.scrollToBottom() {
    scrollBy(0, lastChild.bottom + paddingBottom - (scrollY + height))
}

fun NestedScrollView.scrollToTop() {
    scrollTo(0, 0)
}