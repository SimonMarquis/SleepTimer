package fr.smarquis.sleeptimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fr.smarquis.sleeptimer.SleepNotification.handle
import fr.smarquis.sleeptimer.SleepTileService.Companion.requestTileUpdate

class SleepActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.handle(intent)
        context.requestTileUpdate()
    }
}
