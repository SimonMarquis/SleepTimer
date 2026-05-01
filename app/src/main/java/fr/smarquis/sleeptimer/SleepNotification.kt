package fr.smarquis.sleeptimer

import android.app.AlarmManager.RTC_WAKEUP
import android.app.Notification
import android.app.Notification.CATEGORY_EVENT
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.widget.Toast
import fr.smarquis.sleeptimer.SleepAction.CANCEL
import fr.smarquis.sleeptimer.SleepAction.DECREMENT
import fr.smarquis.sleeptimer.SleepAction.INCREMENT
import fr.smarquis.sleeptimer.SleepTimer.REQUIRES_FOREGROUND_SERVICE
import java.lang.System.currentTimeMillis
import java.text.DateFormat
import java.text.DateFormat.SHORT
import java.util.Date
import java.util.concurrent.TimeUnit.MINUTES

object SleepNotification {

    val TIMEOUT_INITIAL_MILLIS = MINUTES.toMillis(30)
    val TIMEOUT_INCREMENT_MILLIS = MINUTES.toMillis(10)
    val TIMEOUT_DECREMENT_MILLIS = MINUTES.toMillis(10)
    private const val SLEEP_INTENT_EXTRA_KEY = "extras:SleepPendingIntent"

    fun Context.find() = notificationManager().activeNotifications?.firstOrNull { it.id == R.id.notification_id }?.notification

    /**
     * @return a [Boolean] hint indicating the expected "visibility" state.
     */
    fun Context.toggle(): Boolean = if (find() == null) { show(); true } else { cancel(); false }

    fun Context.cancel() = notificationManager().cancel(R.id.notification_id)

    fun Context.update(delta: Long) = find()?.let { existing ->
        val remaining = existing.`when` - currentTimeMillis()
        show(timeout = (remaining + delta).takeIf { it > 0 } ?: remaining, existing)
    }

    fun Context.show(timeout: Long = TIMEOUT_INITIAL_MILLIS, existing: Notification? = null) {
        require(timeout > 0)
        val eta = currentTimeMillis() + timeout
        // Keep track of the original PendingIntent inside the notification's extras because
        // we can't rely on the Notification `deleteIntent` when `REQUIRES_FOREGROUND_SERVICE`
        val sleepPendingIntent = when {
            !REQUIRES_FOREGROUND_SERVICE -> existing?.deleteIntent
            else -> existing?.extras?.getParcelable(SLEEP_INTENT_EXTRA_KEY, PendingIntent::class.java)
        } ?: SleepAudioService.pendingIntent(this)
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
            .apply {
                if (!REQUIRES_FOREGROUND_SERVICE) setDeleteIntent(sleepPendingIntent)
                else extras.putParcelable(SLEEP_INTENT_EXTRA_KEY, sleepPendingIntent)
            }
            .addAction(INCREMENT.action(this).build())
            .addAction(DECREMENT.action(this, cancel = timeout <= TIMEOUT_DECREMENT_MILLIS).build())
            .addAction(CANCEL.action(this).build())
            .build()
        createNotificationChannel()
        notificationManager().notify(R.id.notification_id, notification)

        if (REQUIRES_FOREGROUND_SERVICE) {
            if (alarmManager().canScheduleExactAlarms().not()) Toast.makeText(this, R.string.toast_alarm_permission, Toast.LENGTH_LONG).show()
            else alarmManager().setExactAndAllowWhileIdle(RTC_WAKEUP, eta, sleepPendingIntent)
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