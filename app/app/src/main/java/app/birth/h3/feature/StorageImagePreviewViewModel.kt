package app.birth.h3.feature

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import app.birth.h3.local.AppDatabase
import app.birth.h3.local.entity.StorageImages
import app.birth.h3.repository.AnalyticsRepository
import app.birth.h3.repository.SharePreferenceRepository
import app.birth.h3.util.FileUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StorageImagePreviewViewModel @Inject constructor(
        @ApplicationContext val context: Context,
        val spf: SharePreferenceRepository,
        val analytics: AnalyticsRepository,
        val database: AppDatabase
) : ViewModel() {
    val storageImage = MutableLiveData<StorageImages>()
    val loadBitmap = MutableLiveData<Bitmap>()

    val imageName = MutableLiveData<String>()

    fun loadImage(imageId: Int) {
        database.storageImagesDao().selectById(imageId).onEach {
            Timber.d("load image ${it}")
            storageImage.postValue(it)
            imageName.postValue(it.name)
            FileUtil(context).loadOriginalImage(it)?.let { bitmap ->
                loadBitmap.postValue(bitmap)
            }
        }.catch {
            Timber.e(it)
        }.launchIn(viewModelScope)
    }

    fun onClickPopback(v: View) {
        v.findNavController().popBackStack()
    }
}
