package fr.smarquis.sleeptimer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.provider.Settings
import android.service.quicksettings.Tile.STATE_ACTIVE
import android.service.quicksettings.Tile.STATE_INACTIVE
import android.service.quicksettings.TileService
import fr.smarquis.sleeptimer.SleepNotification.find
import fr.smarquis.sleeptimer.SleepNotification.handle
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
        true -> toggle().also { refreshTile() }
        else -> requestNotificationsPermission()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handle(intent)
        requestTileUpdate()
        stopSelfResult(startId)
        return START_NOT_STICKY
    }

    private fun refreshTile() = qsTile?.run {
        when (val notification = find()) {
            null -> {
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
    }.let(::startActivityAndCollapse)

}