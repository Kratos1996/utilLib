package com.techhub.util.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager

class SilentModeHelper(private val context: Context) {
    interface SilentModeChangeListener {
        fun onSilentModeChanged(isSilent: Boolean)
    }

    private val audioManager: AudioManager by lazy {

        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private var listener: SilentModeChangeListener? = null

    fun setSilentModeChangeListener(listener: SilentModeChangeListener) {
        this.listener = listener
        // Register the listener for changes in ringer mode
        context.registerReceiver(
            ringerModeReceiver,
            IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION)
        )
    }

    fun removeSilentModeChangeListener() {
        listener = null
        // Unregister the listener when it's no longer needed
        try {
            context.unregisterReceiver(ringerModeReceiver)
        } catch (e: Exception) {

        }

    }

    val ringerModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == AudioManager.RINGER_MODE_CHANGED_ACTION) {
                val ringerMode = audioManager.ringerMode
                val isSilent = ringerMode != AudioManager.RINGER_MODE_NORMAL
                listener?.onSilentModeChanged(isSilent)
            }
        }
    }
}
