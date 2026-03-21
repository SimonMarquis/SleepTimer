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
import fr.smarquis.sleeptimer.SleepNotification.handle
import fr.smarquis.sleeptimer.SleepNotification.notificationManager
import fr.smarquis.sleeptimer.SleepNotification.toggle
import java.text.DateFormat.SHORT
import java.text.DateFormat.getTimeInstance
import java.util.Date

class SleepTileService : TileService() {

    companion object {
        fun Context.requestTileUpdate() {
            requestListeningState(this, ComponentName(this, SleepTileService::class.java))
        }
    }

    override fun onStartListening() {
        refreshTile()
    }

    override fun onClick() {
        val notificationsEnabled = notificationManager()?.areNotificationsEnabled() == true

        if (!notificationsEnabled) {
            requestNotificationsPermission()
            return
        }

        toggle()
        refreshTile()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handle(intent)
        requestTileUpdate()
        stopSelfResult(startId)
        return START_NOT_STICKY
    }

    private fun refreshTile() {
        val tile = qsTile ?: return
        val notification = find()

        if (notification == null) {
            tile.state = STATE_INACTIVE
            if (SDK_INT >= Q) tile.subtitle = resources.getText(R.string.tile_subtitle)
            tile.updateTile()
            return
        }

        tile.state = STATE_ACTIVE
        if (SDK_INT >= Q) tile.subtitle = getTimeInstance(SHORT).format(Date(notification.`when`))
        tile.updateTile()
    }

    private fun requestNotificationsPermission() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }

        @SuppressLint("StartActivityAndCollapseDeprecated")
        if (SDK_INT <= TIRAMISU) {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
            return
        }

        startActivityAndCollapse(getActivity(this, 0, intent, FLAG_IMMUTABLE))
    }
}
