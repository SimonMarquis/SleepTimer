package fr.smarquis.sleeptimer

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.ADJUST_LOWER
import android.media.AudioManager.STREAM_MUSIC
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_MEDIA_PAUSE
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

        // pause media
        dispatchMediaKeyEvent(KeyEvent(ACTION_DOWN, KEYCODE_MEDIA_PAUSE))
        dispatchMediaKeyEvent(KeyEvent(ACTION_UP, KEYCODE_MEDIA_PAUSE))

        // restore volume
        Thread.sleep(RESTORE_VOLUME_MILLIS)
        setStreamVolume(STREAM_MUSIC, volumeIndex, 0)

        // update tile
        requestTileUpdate()
    } ?: Unit

}