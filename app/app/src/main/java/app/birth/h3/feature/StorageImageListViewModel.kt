package app.birth.h3.feature

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import app.birth.h3.local.AppDatabase
import app.birth.h3.local.entity.StorageImages
import app.birth.h3.repository.AnalyticsRepository
import app.birth.h3.repository.SharePreferenceRepository
import app.birth.h3.util.FileUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StorageImageListViewModel @Inject constructor(
        @ApplicationContext val context: Context,
        val spf: SharePreferenceRepository,
        val analytics: AnalyticsRepository,
        val database: AppDatabase
) : ViewModel(), LifecycleObserver {
    private var fileUtil: FileUtil? = null

    private fun loadImage() {
        val list = fileUtil?.loadImages()
        Timber.d("load bitmaps = ${list.toString()}")
        if(!list.isNullOrEmpty()) insertStorageImage(list)
    }

    private fun insertStorageImage(list: List<StorageImages>) {
        CoroutineScope(Dispatchers.IO).launch {
            database.storageImagesDao().insertAll(list)
        }
    }

    fun onClickImage(v: View, storageImages: StorageImages) {
        val action = StorageImageListFragmentDirections.actionStorageImageListFragmentToStorageImagePreviewFragment(storageImages.id)
        v.findNavController().navigate(action)
    }

    val flow = Pager(PagingConfig(10), 0) {
        database.storageImagesDao().select()
    }.flow

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        fileUtil = FileUtil(context)
        loadImage()
    }
}
