package com.simats.formsahayak.logic

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

class TtsHelper(context: Context) {
    private var tts: TextToSpeech? = null
    private var isReady = false
    private var currentSpeechRate: Float = 1.0f

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isReady = true
                tts?.setSpeechRate(currentSpeechRate)
            }
        }
    }

    fun setSpeechRate(rate: Float) {
        currentSpeechRate = rate
        if (isReady) {
            tts?.setSpeechRate(rate)
        }
    }

    fun speak(text: String, languageCode: String) {
        if (!isReady) return

        val locale = when (languageCode) {
            "te" -> Locale("te", "IN")
            "ta" -> Locale("ta", "IN")
            "hi" -> Locale("hi", "IN")
            else -> Locale.US
        }

        tts?.language = locale
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.shutdown()
    }
}
