package fr.smarquis.sleeptimer

import android.os.Build

object SleepTimer {
    /**
     * Android 17 introduced [Background audio hardening](https://developer.android.com/about/versions/17/changes/bg-audio) which prevents calling [android.media.AudioManager.adjustStreamVolume] from a background thread.
     */
    val REQUIRES_FOREGROUND_SERVICE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.CINNAMON_BUN
}