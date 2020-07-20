package com.pharmacy.myapp.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pharmacy.myapp.core.extensions.SharedPreferenceContext
import com.pharmacy.myapp.core.extensions.get
import com.pharmacy.myapp.core.extensions.put

class SPManager(val context: Context) : SharedPreferenceContext {

    override val sp: SharedPreferences =
        context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    var token: String?
        get() = get(Keys.TOKEN)
        set(value) {
            put(Keys.TOKEN, value)
        }

    var refreshToken: String?
        get() = get(Keys.REFRESH_TOKEN)
        set(value) {
            put(Keys.REFRESH_TOKEN, value)
        }

    var email: String?
        get() = get(Keys.EMAIL)
        set(value) {
            put(Keys.EMAIL, value)
        }

    var phone: String?
        get() = get(Keys.PHONE)
        set(value) {
            put(Keys.PHONE, value)
        }

    var username: String?
        get() = get(Keys.USERNAME)
        set(value) {
            put(Keys.USERNAME, value)
        }

    fun clear() = sp.edit {
        sp.all.forEach {
            remove(it.key)
        }
    }


    private enum class Keys {
        TOKEN, EMAIL, PHONE, USERNAME, REFRESH_TOKEN
    }
}