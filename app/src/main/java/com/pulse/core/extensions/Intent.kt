package com.pulse.core.extensions

import android.content.Intent

fun Intent.newTask() = addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

fun Intent.clearTask() = addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

fun Intent.singleTop() = addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)