package com.pharmacy.myapp.core.general.interfaces

import android.content.Context

interface ILocaleHelper {

    fun createLocalisedContext(context: Context): Context

}