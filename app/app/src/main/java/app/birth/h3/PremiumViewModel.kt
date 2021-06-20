package app.birth.h3

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.birth.h3.model.TextToSpeechState
import app.birth.h3.repository.AnalyticsRepository
import app.birth.h3.repository.AuthRepository
import app.birth.h3.repository.FunctionsRepository
import app.birth.h3.repository.TextToSpeechRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
        val textToSpeech: TextToSpeechRepository,
        val authRepository: AuthRepository,
        val functionsRepository: FunctionsRepository,
        val analytics: AnalyticsRepository
): ViewModel() {
    private var queueSperk = ""

    init {
        textToSpeech.ttsInitializeState.onEach {
            Timber.d("state = ${it.toString()}")
            if(it == TextToSpeechState.READY_SUCCESS && !queueSperk.isBlank()) {
                sperk(queueSperk)
            }
        }.launchIn(viewModelScope)
    }

    fun queueSperk(message: String) {
        if(textToSpeech.ttsInitializeState.value == TextToSpeechState.READY_SUCCESS) {
            sperk(message)
        } else {
            queueSperk = message
        }
    }

    fun ttsInitilize() {
        textToSpeech.new()
    }

    private fun sperk(message: String) {
        if(textToSpeech.ttsInitializeState.value == TextToSpeechState.READY_SUCCESS) {
            textToSpeech.sperk(message)
        } else {
            Timber.e("text to speech not ready ${textToSpeech.ttsInitializeState.value}")
        }
        queueSperk = ""
    }

    fun intentFirebaseUI() = authRepository.intentFirebaseUI()

    fun saveUser(user: FirebaseUser) = authRepository.saveUser(user)

    fun compressBitmap(bitmap: Bitmap, maxDimension: Int = 1280): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension
        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth =
                    (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight =
                    (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }

    fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.getEncoder().encodeToString(imageBytes)
    }

    fun functionsInitilize() {
        functionsRepository.initilize()
    }

    fun annotateImage(base64encoded: String, onSuccess: (Task<JsonElement>) -> Unit, onFailed: () -> Unit) = functionsRepository.annotateImage(functionsRepository.buildAnnotateImageRequest(base64encoded).toString())?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            onSuccess(task)
        } else {
            onFailed()
        }
    }

    fun annotationText(task: Task<JsonElement>): String {
        val annotation = task.result.asJsonArray[0].asJsonObject["fullTextAnnotation"].asJsonObject
        return annotation["text"].asString
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech.shutDown()
    }
}
