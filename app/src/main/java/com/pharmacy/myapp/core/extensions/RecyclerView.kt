package com.pharmacy.myapp.core.extensions

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onScrollStateChanged(listener: (RecyclerView, Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
            listener.invoke(recyclerView, state)
        }
    })
}

fun RecyclerView.onScrollStateChanged(listener: (Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
            listener.invoke(state)
        }
    })
}

fun RecyclerView.onScrolled(listener: (RecyclerView, Int, Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            listener.invoke(recyclerView, dx, dy)
        }
    })
}

fun RecyclerView.addAutoKeyboardCloser(){
    onScrollStateChanged { state ->
        if (state == RecyclerView.SCROLL_STATE_DRAGGING && isKeyboardOpen) {
            hideKeyboard()
        }
    }
}