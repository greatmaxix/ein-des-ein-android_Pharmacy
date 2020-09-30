package com.pharmacy.myapp.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.SharedPreferenceContext
import com.pharmacy.myapp.core.extensions.get
import com.pharmacy.myapp.core.extensions.put
import com.pharmacy.myapp.core.general.interfaces.ManagerInterface

class SPManager(val context: Context) : SharedPreferenceContext, ManagerInterface {

    override val sp: SharedPreferences = context.getSharedPreferences("${context.getString(R.string.app_name)}_sp", Context.MODE_PRIVATE)

    var token: String?
        get() = get(Keys.TOKEN)
        set(value) { put(Keys.TOKEN, value) }

    var refreshToken: String?
        get() = get(Keys.REFRESH_TOKEN)
        set(value) { put(Keys.REFRESH_TOKEN, value) }

    val isTokenExists: Boolean get() = !token.isNullOrEmpty() && !refreshToken.isNullOrEmpty()

    var qrCodeDescriptionShown: Boolean?
        get() = get(Keys.QR_CODE_DESCRIPTION_SHOWN)
        set(value) = put(Keys.QR_CODE_DESCRIPTION_SHOWN, value)

    val isUserLogin
        get() = refreshToken?.isNotEmpty() ?: false

    var isNeedOnBoarding: Boolean
        get() = get(Keys.IS_ONBOARDING_SHOWN) ?: false
        set(value) = put(Keys.IS_ONBOARDING_SHOWN, value)

    var regionId: Int?
        get() = get(Keys.REGION_ID)
        set(value) { value?.let { put(Keys.REGION_ID, it) } }

    override fun clear() = sp.edit {
        sp.all.forEach {
            remove(it.key)
        }
    }

    private enum class Keys {
        TOKEN, REFRESH_TOKEN, QR_CODE_DESCRIPTION_SHOWN, IS_ONBOARDING_SHOWN, REGION_ID, REGION_NAME
    }
}