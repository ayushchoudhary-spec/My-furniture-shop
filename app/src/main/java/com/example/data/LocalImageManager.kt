package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

object LocalImageManager {
  private const val DIR_NAME = "catalog_photos"
  const val MAX_CATALOGUE_CAPACITY = 200

  fun getImageDirectory(context: Context): File {
    val dir = File(context.filesDir, DIR_NAME)
    if (!dir.exists()) {
      dir.mkdirs()
    }
    return dir
  }

  suspend fun saveImageFromUri(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
    try {
      val dir = getImageDirectory(context)
      val fileName = "furniture_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}.jpg"
      val destFile = File(dir, fileName)

      val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
      if (inputStream != null) {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        if (bitmap != null) {
          // Scale down if extremely large to optimize memory and offline storage
          val maxDim = 1280
          val scaledBitmap = if (bitmap.width > maxDim || bitmap.height > maxDim) {
            val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
            val (targetW, targetH) = if (ratio > 1f) {
              maxDim to (maxDim / ratio).toInt()
            } else {
              (maxDim * ratio).toInt() to maxDim
            }
            Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
          } else {
            bitmap
          }

          FileOutputStream(destFile).use { out ->
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 88, out)
          }
          if (scaledBitmap != bitmap) {
            bitmap.recycle()
          }
          scaledBitmap.recycle()
          return@withContext destFile.absolutePath
        }
      }
      null
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  suspend fun deleteLocalImage(filePath: String?) = withContext(Dispatchers.IO) {
    if (filePath != null && filePath.startsWith("/")) {
      try {
        val file = File(filePath)
        if (file.exists()) {
          file.delete()
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }
}
