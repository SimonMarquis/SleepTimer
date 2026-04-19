package fr.smarquis.sleeptimer

import android.app.AlarmManager.RTC_WAKEUP
import android.app.Notification
import android.app.Notification.CATEGORY_EVENT
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.widget.Toast
import fr.smarquis.sleeptimer.SleepNotification.Action.CANCEL
import fr.smarquis.sleeptimer.SleepNotification.Action.DECREMENT
import fr.smarquis.sleeptimer.SleepNotification.Action.INCREMENT
import fr.smarquis.sleeptimer.SleepTimer.REQUIRES_FOREGROUND_SERVICE
import java.lang.System.currentTimeMillis
import java.text.DateFormat
import java.text.DateFormat.SHORT
import java.util.Date
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES

object SleepNotification {

    private val TIMEOUT_INITIAL_MILLIS = MINUTES.toMillis(30)
    private val TIMEOUT_INCREMENT_MILLIS = MINUTES.toMillis(10)
    private val TIMEOUT_DECREMENT_MILLIS = MINUTES.toMillis(10)

    private enum class Action(private val value: String) {
        CANCEL("fr.smarquis.sleeptimer.action.CANCEL") {
            override fun title(context: Context) = context.getText(android.R.string.cancel)
        },
        INCREMENT("fr.smarquis.sleeptimer.action.INCREMENT") {
            override fun title(context: Context) = "+" + MILLISECONDS.toMinutes(TIMEOUT_INCREMENT_MILLIS)
        },
        DECREMENT("fr.smarquis.sleeptimer.action.DECREMENT") {
            override fun title(context: Context) = "-" + MILLISECONDS.toMinutes(TIMEOUT_DECREMENT_MILLIS)
        },
        ;

        companion object {
            fun parse(value: String?): Action? = entries.firstOrNull { it.value == value }
        }

        private fun intent(context: Context): Intent = Intent(context, SleepActionReceiver::class.java).setAction(value)

        private fun pendingIntent(context: Context, cancel: Boolean = false): PendingIntent? =
            PendingIntent.getBroadcast(context, 0, intent(context), FLAG_IMMUTABLE).apply { if (cancel) cancel() }

        fun action(context: Context, cancel: Boolean = false): Notification.Action.Builder =
            Notification.Action.Builder(Icon.createWithResource(context, 0), title(context), pendingIntent(context, cancel))

        abstract fun title(context: Context): CharSequence?
    }

    fun Context.find() = notificationManager().activeNotifications?.firstOrNull { it.id == R.id.notification_id }?.notification

    fun Context.handle(intent: Intent?) = when (Action.parse(intent?.action)) {
        INCREMENT -> update(TIMEOUT_INCREMENT_MILLIS)
        DECREMENT -> update(-TIMEOUT_DECREMENT_MILLIS)
        CANCEL -> cancel()
        null -> Unit
    }

    /**
     * @return a [Boolean] hint indicating the expected "visibility" state.
     */
    fun Context.toggle(): Boolean = if (find() == null) { show(); true } else { cancel(); false }

    private fun Context.cancel() = notificationManager().cancel(R.id.notification_id)

    private fun Context.update(delta: Long) = find()?.let { existing ->
        val remaining = existing.`when` - currentTimeMillis()
        show(timeout = (remaining + delta).takeIf { it > 0 } ?: remaining, existing)
    }

    private fun Context.show(timeout: Long = TIMEOUT_INITIAL_MILLIS, existing: Notification? = null) {
        require(timeout > 0)
        val eta = currentTimeMillis() + timeout
        val notification = Notification.Builder(this, getString(R.string.notification_channel_id))
            .setCategory(CATEGORY_EVENT)
            .setVisibility(VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_tile)
            .setSubText(DateFormat.getTimeInstance(SHORT).format(Date(eta)))
            .setShowWhen(true).setWhen(eta)
            .setUsesChronometer(true).setChronometerCountDown(true)
            .setTimeoutAfter(timeout)
            .apply { if (!REQUIRES_FOREGROUND_SERVICE) setDeleteIntent(existing?.deleteIntent ?: SleepAudioService.pendingIntent(this@show)) }
            .addAction(INCREMENT.action(this).build())
            .addAction(DECREMENT.action(this, cancel = timeout <= TIMEOUT_DECREMENT_MILLIS).build())
            .addAction(CANCEL.action(this).build())
            .build()
        createNotificationChannel()
        notificationManager().notify(R.id.notification_id, notification)

        if (REQUIRES_FOREGROUND_SERVICE) {
            if (alarmManager().canScheduleExactAlarms().not()) Toast.makeText(this, R.string.toast_alarm_permission, Toast.LENGTH_LONG).show()
            else alarmManager().setExactAndAllowWhileIdle(RTC_WAKEUP, eta, SleepAudioService.pendingIntent(this@show))
        }
    }

    private fun Context.createNotificationChannel() {
        val id = getString(R.string.notification_channel_id)
        val name: CharSequence = getString(R.string.app_name)
        val channel = NotificationChannel(id, name, IMPORTANCE_LOW).apply {
            setBypassDnd(true)
            lockscreenVisibility = VISIBILITY_PUBLIC
        }
        notificationManager().createNotificationChannel(channel)
    }

}