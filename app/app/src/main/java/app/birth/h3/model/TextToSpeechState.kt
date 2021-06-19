package app.birth.h3.model

sealed class TextToSpeechState {
    object NOT_READY: TextToSpeechState()
    object READY_SUCCESS: TextToSpeechState()
    object SPERK_START: TextToSpeechState()
    object SPERK_DONE: TextToSpeechState()
    data class ERROR(val message: String): TextToSpeechState()
}
