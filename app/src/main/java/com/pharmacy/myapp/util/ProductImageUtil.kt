package com.pharmacy.myapp.util

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.loadGlide
import com.pharmacy.myapp.product.model.ProductLite

object ProductImageUtil {

    fun setProductImage(imageView: ImageView, product: ProductLite) {
        imageView.loadGlide(product.pictures.firstOrNull()?.url) {
            transform(CenterCrop(), RoundedCorners(imageView.resources.getDimensionPixelSize(R.dimen._8sdp)))
            error(R.drawable.default_product_image)
        }
        val hasPictures = product.pictures.isNotEmpty()
        imageView.setBackgroundColor(if (hasPictures) 0 else ContextCompat.getColor(imageView.context, R.color.mediumGrey50))
        imageView.colorFilter = (if (product.aggregation == null && !hasPictures) ColorFilterUtil.blackWhiteFilter else null)
    }

}