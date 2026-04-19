package fr.smarquis.sleeptimer

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager


fun Context.alarmManager(): AlarmManager = getSystemService(AlarmManager::class.java)
fun Context.audioManager(): AudioManager = getSystemService(AudioManager::class.java)
fun Context.notificationManager(): NotificationManager = getSystemService(NotificationManager::class.java)
