package app.birth.h3.feature

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import app.birth.h3.local.AppDatabase
import app.birth.h3.local.entity.StorageImages
import app.birth.h3.repository.AnalyticsRepository
import app.birth.h3.repository.SharePreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StorageImagePreviewViewModel @Inject constructor(
        val spf: SharePreferenceRepository,
        val analytics: AnalyticsRepository,
        val database: AppDatabase
) : ViewModel() {
    val storageImage = MutableLiveData<StorageImages>()

    fun loadImage(imageId: Int) {
        database.storageImagesDao().selectById(imageId).onEach {
            Timber.d("load image ${it}")
            storageImage.postValue(it)
        }.catch {
            Timber.e(it)
        }.launchIn(viewModelScope)
    }

    fun onClickPopback(v: View) {
        v.findNavController().popBackStack()
    }
}
