package com.example.criminalintent

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import java.io.File
import java.security.AccessController.getContext
import kotlin.math.roundToInt

interface PictureUtils {
    private fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
// Read in the dimensions of the image on disk
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()
// Figure out how much to scale down by
        val sampleSize = if (srcHeight <= destHeight && srcWidth <= destWidth) {
            1
        } else {
            val heightScale = srcHeight / destHeight
            val widthScale = srcWidth / destWidth
            minOf(heightScale, widthScale).roundToInt()
        }
// Read in and create final bitmap
        return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        })
    }
    fun updatePhoto(context: Context, view: ImageView, photoFileName: String?) {
        if (view.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(context.applicationContext.filesDir, it)
            }
            if (photoFile?.exists() == true) {
                view.doOnLayout { measuredView ->
                    val scaledBitmap = getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    view.setImageBitmap(scaledBitmap)
                    view.tag = photoFileName
                }
            } else {
                view.setImageBitmap(null)
                view.tag = null
            }
        }
    }


}