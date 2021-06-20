package app.birth.h3.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import app.birth.h3.BuildConfig
import app.birth.h3.R
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URI

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

    fun newFileName() = "hitsudan_${System.currentTimeMillis()}.png"

    fun saveFile(bitmap: Bitmap, onSuccess: (uri: Uri?) -> Unit, onFailed: (e: Exception) -> Unit) {
        val fileName = newFileName()
        var uri: Uri? = null
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

                uri = item
            } else {
                val file = File(mkdirAppDirectory(), fileName)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))

                uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)
            }
            onSuccess(uri)
        } catch (e: FileSystemException) {
            onFailed(e)
        }
    }

    fun loadImages() {
        try {
            val targetUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val projection: Array<String> = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.Media.RELATIVE_PATH)

            val selection = "${MediaStore.Files.FileColumns.MIME_TYPE}=? AND ${MediaStore.Images.Media.RELATIVE_PATH}=?"

            var selectionArgs = arrayOf("image/png", "Pictures/ひつだん/")

            context.applicationContext.contentResolver.query(
                targetUri,
                projection,
                selection,
                selectionArgs,
                null
            )?.use {
                if(it.count > 0) {
                    while (it.moveToNext()) {
                        val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                        val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                        val pathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)

                        val id = it.getLong(idColumn)
                        val name = it.getString(nameColumn)
                        val path = it.getString(pathColumn)
                        Timber.d("load image id=${id}, path=${path}, name=${name}")
                    }
                }
            }
        } catch (e: FileSystemException) {
            Timber.d("FileSystemException")
            Timber.e(e)
        }
    }
}
