package app.birth.h3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.birth.h3.model.TextToSpeechState
import app.birth.h3.repository.AnalyticsRepository
import app.birth.h3.repository.TextToSpeechRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
        val textToSpeech: TextToSpeechRepository,
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

    override fun onCleared() {
        super.onCleared()
        textToSpeech.shutDown()
    }
}
