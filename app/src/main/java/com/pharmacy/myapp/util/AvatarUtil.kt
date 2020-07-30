package com.pharmacy.myapp.util

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

object AvatarUtil {

    fun saveAvatarToFile(context: Context, avatarUrl: String, actionEnd: () -> Unit = {}) {
        Glide.get(context).clearDiskCache()
        Glide.with(context).asDrawable().listener(object : RequestListener<Drawable?> {

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                actionEnd()
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                val file = File(context.externalCacheDir, Constants.AVATAR_FILE_NAME)
                resource?.let { ImageFileUtil.saveImageDrawable(it, file) }
                actionEnd()
                return true
            }
        }).load(avatarUrl).submit()
    }

}