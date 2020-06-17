package com.pharmacy.myapp.utils.extension

import android.content.Intent
import android.provider.Settings.ACTION_SECURITY_SETTINGS
import androidx.fragment.app.Fragment

fun Fragment.openSecuritySettings() = activity?.startActivityForResult(Intent(ACTION_SECURITY_SETTINGS), 1000)