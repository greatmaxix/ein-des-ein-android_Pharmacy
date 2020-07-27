package com.pharmacy.myapp.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.SharedPreferenceContext
import com.pharmacy.myapp.core.extensions.get
import com.pharmacy.myapp.core.extensions.put

class SPManager(val context: Context) : SharedPreferenceContext {

    override val sp: SharedPreferences =
        context.getSharedPreferences("${context.getString(R.string.app_name)}_sp", Context.MODE_PRIVATE)

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

    var name: String?
        get() = get(Keys.USERNAME)
        set(value) {
            put(Keys.USERNAME, value)
        }

    var avatarUuid: String?
        get() = get(Keys.AVATAR_UUID)
        set(value) {
            put(Keys.AVATAR_UUID, value)
        }

    var avatarUrl: String?
        get() = get(Keys.AVATAR_URL)
        set(value) {
            put(Keys.AVATAR_URL, value)
        }

    var qrCodeDescriptionShown: Boolean?
        get() = get(Keys.QR_CODE_DESCRIPTION_SHOWN)
        set(value) = put(Keys.QR_CODE_DESCRIPTION_SHOWN, value)

    fun clear() = sp.edit {
        sp.all.forEach {
            remove(it.key)
        }
    }

    private enum class Keys {
        TOKEN, EMAIL, PHONE, USERNAME, REFRESH_TOKEN, AVATAR_UUID, AVATAR_URL, QR_CODE_DESCRIPTION_SHOWN
    }
}