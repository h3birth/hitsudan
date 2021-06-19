package app.birth.h3.repository

import android.speech.tts.TextToSpeech
import app.birth.h3.model.TextToSpeechState
import kotlinx.coroutines.flow.StateFlow

interface TextToSpeechRepository {
    var tts: TextToSpeech?

    val ttsInitializeState: StateFlow<TextToSpeechState>

    fun new()

    fun shutDown()

    fun sperk(message: String)
}
