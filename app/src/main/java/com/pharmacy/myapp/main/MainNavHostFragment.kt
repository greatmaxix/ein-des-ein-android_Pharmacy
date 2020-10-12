package com.pharmacy.myapp.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@KoinApiExtension
class MainNavHostFragment : NavHostFragment(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = get()
        super.onCreate(savedInstanceState)
    }
}