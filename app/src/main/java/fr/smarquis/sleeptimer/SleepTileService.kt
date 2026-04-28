package fr.smarquis.sleeptimer

import android.annotation.SuppressLint
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.getActivity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Build.VERSION_CODES.TIRAMISU
import android.provider.Settings
import android.service.quicksettings.Tile.STATE_ACTIVE
import android.service.quicksettings.Tile.STATE_INACTIVE
import android.service.quicksettings.TileService
import android.widget.Toast
import fr.smarquis.sleeptimer.SleepNotification.find
import fr.smarquis.sleeptimer.SleepNotification.toggle
import fr.smarquis.sleeptimer.SleepTimer.REQUIRES_FOREGROUND_SERVICE
import java.text.DateFormat.SHORT
import java.text.DateFormat.getTimeInstance
import java.util.Date

class SleepTileService : TileService() {

    companion object {
        fun Context.requestTileUpdate() = requestListeningState(this, ComponentName(this, SleepTileService::class.java))
    }

    override fun onStartListening() = refreshTile()

    override fun onClick() = when {
        notificationManager().areNotificationsEnabled().not() -> requestNotificationsPermission()
        REQUIRES_FOREGROUND_SERVICE && alarmManager().canScheduleExactAlarms().not() -> requestScheduleExactAlarmsPermission()
        else -> toggle().let(::refreshTile)
    }

    /**
     * @param hint use `false` to force the [STATE_INACTIVE] update
     */
    private fun refreshTile(hint: Boolean? = null) = qsTile?.run {
        val notification = find()
        when {
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

    private fun requestNotificationsPermission() {
        Toast.makeText(this, R.string.toast_notification_permission, Toast.LENGTH_LONG).show()
        startActivityAndCollapseCompat(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, packageName))
    }

    private fun requestScheduleExactAlarmsPermission() {
        Toast.makeText(this, R.string.toast_alarm_permission, Toast.LENGTH_LONG).show()
        if (SDK_INT >= Build.VERSION_CODES.S) startActivityAndCollapseCompat(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:$packageName")))
    }

    private fun startActivityAndCollapseCompat(intent: Intent) {
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        @SuppressLint("StartActivityAndCollapseDeprecated")
        if (SDK_INT <= TIRAMISU) @Suppress("DEPRECATION") startActivityAndCollapse(intent)
        else startActivityAndCollapse(getActivity(this, 0, intent, FLAG_IMMUTABLE))
    }
}
