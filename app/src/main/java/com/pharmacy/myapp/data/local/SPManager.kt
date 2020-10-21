package com.pharmacy.myapp.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.SharedPreferenceContext
import com.pharmacy.myapp.core.extensions.get
import com.pharmacy.myapp.core.extensions.put
import com.pharmacy.myapp.core.general.interfaces.ManagerInterface
import com.pharmacy.myapp.model.auth.token.TokenModel

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
        set(value) {
            value?.let { put(Keys.REGION_ID, it) }
        }

    fun saveTokens(new: TokenModel) {
        token = new.token
        refreshToken = new.refreshToken
    }

    var openedChatId: Int?
        get() = get(Keys.OPENED_CHAT_ID)
        set(value) {
            put(Keys.OPENED_CHAT_ID, value)
        }

    fun clearChatId() {
        sp.edit { remove(Keys.OPENED_CHAT_ID.name) }
    }

    var isChatForeground: Boolean?
        get() = get(Keys.IS_CHAT_FOREGROUND)
        set(value) = put(Keys.IS_CHAT_FOREGROUND, value)

    override fun clear() = sp.edit {
        sp.all.forEach {
            remove(it.key)
        }
    }

    private enum class Keys {
        TOKEN, REFRESH_TOKEN, QR_CODE_DESCRIPTION_SHOWN, IS_ONBOARDING_SHOWN, REGION_ID, OPENED_CHAT_ID, IS_CHAT_FOREGROUND
    }
}