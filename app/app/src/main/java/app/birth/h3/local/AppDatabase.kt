package app.birth.h3.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.birth.h3.local.converter.BitmapConverter
import app.birth.h3.local.dao.StorageImagesDao
import app.birth.h3.local.entity.StorageImages
import com.google.android.datatransport.runtime.dagger.Provides
import com.google.android.datatransport.runtime.dagger.Subcomponent

@Database(entities = arrayOf(
        StorageImages::class
), version = 1, exportSchema = false)
@TypeConverters(BitmapConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storageImagesDao(): StorageImagesDao
}
