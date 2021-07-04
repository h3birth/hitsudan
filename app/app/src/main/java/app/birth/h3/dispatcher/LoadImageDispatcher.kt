package app.birth.h3.dispatcher

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class LoadImageDispatcher {
    private val _loadImage = MutableStateFlow<Bitmap?>(null)
    val loadImage: SharedFlow<Bitmap?> get() = _loadImage
    suspend fun emitLoadImage(bitmap: Bitmap?) {
        _loadImage.value = bitmap
    }
    suspend fun deleteLoadImage() {
        _loadImage.value = null
    }
}
