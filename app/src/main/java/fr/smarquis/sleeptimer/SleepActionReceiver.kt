package fr.smarquis.sleeptimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fr.smarquis.sleeptimer.SleepAction.CANCEL
import fr.smarquis.sleeptimer.SleepAction.DECREMENT
import fr.smarquis.sleeptimer.SleepAction.INCREMENT
import fr.smarquis.sleeptimer.SleepAction.START
import fr.smarquis.sleeptimer.SleepAction.UPDATE
import fr.smarquis.sleeptimer.SleepNotification.TIMEOUT_DECREMENT_MILLIS
import fr.smarquis.sleeptimer.SleepNotification.TIMEOUT_INCREMENT_MILLIS
import fr.smarquis.sleeptimer.SleepNotification.TIMEOUT_INITIAL_MILLIS
import fr.smarquis.sleeptimer.SleepNotification.cancel
import fr.smarquis.sleeptimer.SleepNotification.show
import fr.smarquis.sleeptimer.SleepNotification.update
import fr.smarquis.sleeptimer.SleepTileService.Companion.requestTileUpdate

class SleepActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.handle(intent)
        context.requestTileUpdate()
    }

    fun Context.handle(intent: Intent?) = when (SleepAction.parse(intent)) {
        START -> show(timeout = SleepAction.duration(intent) ?: TIMEOUT_INITIAL_MILLIS)
        UPDATE -> update(delta = SleepAction.duration(intent) ?: 0L)
        INCREMENT -> update(TIMEOUT_INCREMENT_MILLIS)
        DECREMENT -> update(-TIMEOUT_DECREMENT_MILLIS)
        CANCEL -> cancel()
        null -> Unit
    }
}
