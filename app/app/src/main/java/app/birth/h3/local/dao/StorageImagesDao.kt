package app.birth.h3.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.birth.h3.local.entity.StorageImages
import kotlinx.coroutines.flow.Flow

@Dao
interface StorageImagesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(storageImages: List<StorageImages>)

    @Query("SELECT * FROM storage_images ORDER BY image_id DESC LIMIT :limit OFFSET :offset")
    fun select(limit: Int = 18, offset: Int = 0): PagingSource<Int, StorageImages>

    @Query("SELECT * FROM storage_images WHERE id = :id LIMIT 1")
    fun selectById(id: Int): Flow<StorageImages>
}
