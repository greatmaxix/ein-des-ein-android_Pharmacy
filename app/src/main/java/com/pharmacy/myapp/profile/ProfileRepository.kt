package com.pharmacy.myapp.profile

import com.pharmacy.myapp.data.local.SPManager

class ProfileRepository(private val spManager: SPManager) {

    fun getUserData() = Triple(spManager.email, spManager.phone, spManager.username)

}