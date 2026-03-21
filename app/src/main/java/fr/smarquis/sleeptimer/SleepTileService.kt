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

class SleepTimerTileService : TileService() {

    override fun onStartListening() {
        val tile = qsTile ?: return
        val isTimerRunning = SleepTimerManager.isTimerActive(applicationContext)
        
        tile.state = if (isTimerRunning) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()
    }

    override fun onClick() {
        val tile = qsTile ?: return
        val isTimerRunning = SleepTimerManager.isTimerActive(applicationContext)
        
        if (isTimerRunning) {
            SleepTimerManager.stopTimer(applicationContext)
            tile.state = Tile.STATE_INACTIVE
            tile.updateTile()
            return
        }
        
        SleepTimerManager.startTimer(applicationContext)
        tile.state = Tile.STATE_ACTIVE
        tile.updateTile()
    }
}
