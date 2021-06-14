package app.birth.h3.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import app.birth.h3.R
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class FileUtil(val context: Context) {
    companion object {
        private val EXTERNAL_PUBLIC_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    }

    fun mkdirAppDirectory(): File {
        val appDir = context.resources.getString(R.string.app_name)
        val dirPath = File(EXTERNAL_PUBLIC_DIRECTORY, appDir)
        dirPath.mkdirs()
        return dirPath
    }

    fun appDirectory() = "${context.resources.getString(R.string.app_name)}"

    fun newFileName() = "${System.currentTimeMillis()}.png"

    fun saveFile(bitmap: Bitmap, onSuccess: () -> Unit, onFailed: (e: Exception) -> Unit) {
        val fileName = newFileName()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + appDirectory())
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                val item = context.contentResolver.insert(collection, values)!!

                context.contentResolver.openFileDescriptor(item, "w", null).use {
                    FileOutputStream(it!!.fileDescriptor).use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    }
                }

                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(item, values, null, null)
            } else {
                val file = File(mkdirAppDirectory(), fileName)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
            }
            onSuccess()
        } catch (e: FileSystemException) {
            onFailed(e)
        }
    }
}
