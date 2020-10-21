package com.pulse.util

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter

object ColorFilterUtil {

    val blackWhiteFilter by lazy { ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) }) }

}