package com.pharmacy.myapp.core.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@Deprecated("")
fun ViewModel.launchIO(action: (CoroutineScope) -> Unit) = viewModelScope.launch(IO) {
    action.invoke(this)
}