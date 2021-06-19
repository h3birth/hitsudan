package app.birth.h3.repository

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import app.birth.h3.model.TextToSpeechState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class TextToSpeechRepositoryImpl @Inject constructor(
        @ApplicationContext val context: Context
): TextToSpeechRepository, TextToSpeech.OnInitListener {
    companion object {
        val PITCH = 1f
        val SPEED = 1f
    }

    override var tts: TextToSpeech? = null
    private val _ttsInitializeState: MutableStateFlow<TextToSpeechState> = MutableStateFlow(TextToSpeechState.NOT_READY)
    override val ttsInitializeState: StateFlow<TextToSpeechState> get() = _ttsInitializeState

    private fun postTtsInitializeStateValue(state: TextToSpeechState) {
        _ttsInitializeState.value = state
    }

    override fun new() {
        tts = TextToSpeech(context, this)
        tts?.apply {
            setSpeechRate(SPEED)
            setPitch(PITCH)
            setOnUtteranceProgressListener(object : UtteranceProgressListener(){
                override fun onDone(utteranceId: String?) {
                    Timber.d("onDone id=$utteranceId")
                    postTtsInitializeStateValue(TextToSpeechState.SPERK_DONE)
                }

                override fun onError(utteranceId: String?) {
                    Timber.d("onError id=$utteranceId")
                    postTtsInitializeStateValue(TextToSpeechState.ERROR("error sperk"))
                }

                override fun onStart(utteranceId: String?) {
                    Timber.d("onStart id=$utteranceId")
                    postTtsInitializeStateValue(TextToSpeechState.SPERK_START)
                }

            })
        }
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS) {
            Timber.d("onInit success")
            val locale = Locale.JAPAN
            if (tts?.isLanguageAvailable(locale) ?: -999 > TextToSpeech.LANG_AVAILABLE) {
                Timber.d("onInit set langauage")
                tts?.language = Locale.JAPAN
                postTtsInitializeStateValue(TextToSpeechState.READY_SUCCESS)
            } else {
                Timber.d("onInit failed langauage")
                postTtsInitializeStateValue(TextToSpeechState.ERROR("error set language"))
            }
        } else {
            Timber.d("onInit failed")
            postTtsInitializeStateValue(TextToSpeechState.ERROR("error init. state = $status"))
        }
    }

    override fun shutDown() {
        tts?.shutdown()
    }

    override fun sperk(message: String) {
        if(tts?.isSpeaking == true) {
            tts?.stop()
            return
        }

        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "messageID")
    }
}
