package app.birth.h3.feature

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import app.birth.h3.repository.AnalyticsRepository
import app.birth.h3.repository.ColorRepository
import app.birth.h3.repository.SharePreferenceRepository
import app.birth.h3.util.FileUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StorageImageListViewModel @Inject constructor(
        val spf: SharePreferenceRepository,
        val analytics: AnalyticsRepository
) : ViewModel(), LifecycleObserver {
    private var fileUtil: FileUtil? = null

    fun init(context: Context) {
        fileUtil = FileUtil(context)
    }

    fun loadImage() {
        val list = fileUtil?.loadImages()
        Timber.d("load bitmaps = ${list.toString()}")
    }
}
