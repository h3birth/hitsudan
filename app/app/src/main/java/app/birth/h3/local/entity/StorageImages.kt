package app.birth.h3.local.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "storage_images", indices = [(Index(value = ["image_id", "name"], unique = true))])
data class StorageImages(
        @PrimaryKey(autoGenerate = true) var id: Int,
        @ColumnInfo(name = "image_id") var imageId: Long,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "thumbnail") var thumbnail: Bitmap?,
        @ColumnInfo(name = "original_Image") var originalImage: Bitmap?
)
