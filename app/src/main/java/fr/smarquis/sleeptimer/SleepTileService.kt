package fr.smarquis.sleeptimer

import android.annotation.SuppressLint
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.getActivity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Build.VERSION_CODES.TIRAMISU
import android.provider.Settings
import android.service.quicksettings.Tile.STATE_ACTIVE
import android.service.quicksettings.Tile.STATE_INACTIVE
import android.service.quicksettings.TileService
import fr.smarquis.sleeptimer.SleepNotification.find
import fr.smarquis.sleeptimer.SleepNotification.notificationManager
import fr.smarquis.sleeptimer.SleepNotification.toggle
import java.text.DateFormat.SHORT
import java.text.DateFormat.getTimeInstance
import java.util.Date

class SleepTileService : TileService() {

    companion object {
        fun Context.requestTileUpdate() = requestListeningState(this, ComponentName(this, SleepTileService::class.java))
    }

    override fun onStartListening() = refreshTile()

    override fun onClick() = when (notificationManager()?.areNotificationsEnabled()) {
        true -> toggle().let(::refreshTile)
        else -> requestNotificationsPermission()
    }

    /**
     * @param hint use `false` to force the [STATE_INACTIVE] update
     */
    private fun refreshTile(hint: Boolean? = null) = qsTile?.run {
        val notification = find()
        when  {
            // The canceled notification might still be considered active by NotificationManager... so we use an extra hint
            notification == null || hint == false -> {
                state = STATE_INACTIVE
                if (SDK_INT >= Q) subtitle = resources.getText(R.string.tile_subtitle)
            }
            else -> {
                state = STATE_ACTIVE
                if (SDK_INT >= Q) subtitle = getTimeInstance(SHORT).format(Date(notification.`when`))
            }
        }
        updateTile()
    } ?: Unit

    private fun requestNotificationsPermission() = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    }.let {
        @SuppressLint("StartActivityAndCollapseDeprecated")
        if (SDK_INT <= TIRAMISU) @Suppress("DEPRECATION") startActivityAndCollapse(it)
        else startActivityAndCollapse(getActivity(this, 0, it, FLAG_IMMUTABLE))
    }

}