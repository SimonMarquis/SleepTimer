package fr.smarquis.sleeptimer

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_MUSIC
import android.media.AudioAttributes.USAGE_MEDIA
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.ADJUST_LOWER
import android.media.AudioManager.AUDIOFOCUS_GAIN
import android.media.AudioManager.STREAM_MUSIC
import fr.smarquis.sleeptimer.SleepTileService.Companion.requestTileUpdate
import java.util.concurrent.TimeUnit.SECONDS

@Suppress("DEPRECATION")
class SleepAudioService : android.app.IntentService("SleepAudioService") {

    companion object {
        private val FADE_STEP_MILLIS = SECONDS.toMillis(1)
        private val RESTORE_VOLUME_MILLIS = SECONDS.toMillis(2)

        private fun intent(context: Context) = Intent(context, SleepAudioService::class.java)
        fun pendingIntent(context: Context): PendingIntent? = PendingIntent.getService(context, 0, intent(context), FLAG_IMMUTABLE)
    }

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) = getSystemService(AudioManager::class.java)?.run {
        val volumeIndex = getStreamVolume(STREAM_MUSIC)

        // fade out volume
        do {
            adjustStreamVolume(STREAM_MUSIC, ADJUST_LOWER, 0)
            Thread.sleep(FADE_STEP_MILLIS)
        } while (getStreamVolume(STREAM_MUSIC) > 0)

        // request focus
        val attributes = AudioAttributes.Builder().setUsage(USAGE_MEDIA).setContentType(CONTENT_TYPE_MUSIC).build()
        val focusRequest = AudioFocusRequest.Builder(AUDIOFOCUS_GAIN).setAudioAttributes(attributes).setOnAudioFocusChangeListener {}.build()
        requestAudioFocus(focusRequest)
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
        bluetoothAdapter.getAdapter().disable();

        // restore volume
        Thread.sleep(RESTORE_VOLUME_MILLIS)
        setStreamVolume(STREAM_MUSIC, volumeIndex, 0)
        abandonAudioFocusRequest(focusRequest)

        // update tile
        requestTileUpdate()
    } ?: Unit

}
