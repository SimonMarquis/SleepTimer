# Sleep Timer

[![Android CI](https://github.com/SimonMarquis/SleepTimer/workflows/Android%20CI/badge.svg)](https://github.com/SimonMarquis/SleepTimer/actions/workflows/android.yml)
[![Google Play](https://img.shields.io/badge/Google_Play-black?style=flat&logo=google-play&logoColor=white)](https://play.google.com/store/apps/details?id=fr.smarquis.sleeptimer)
[![F-Droid](https://img.shields.io/f-droid/v/fr.smarquis.sleeptimer?style=flat&logo=f-droid&logoColor=white&label=F-Droid&labelColor=black&color=black)](https://f-droid.org/en/packages/fr.smarquis.sleeptimer/)
[![IzzyOnDroid](https://img.shields.io/endpoint?url=https%3A%2F%2Fapt.izzysoft.de%2Ffdroid%2Fapi%2Fv1%2Fshield%2Ffr.smarquis.sleeptimer&style=flat&labelColor=black&color=black&logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAAA4VBMVEXn9cuv7wDB9iGp4x2k5gKh3B6k3SyAxAGd4ASo6gCv5SCW2gHA7UTB6V%2BEwiOw3lK36zC%2B422d1yO78SWs3kfR7JhQiw2751G7%2BQCz8gCKzgGq3zay5DSm2jrF9jZLfwmNyiC77zXO7oaYzjW37CLj9Lze8LLA43uz3mK19ACR1QBcnRO78R6ExBek1kbE8FLI6nSPu0jH5YJxtQ2b1RiAmz53uwF7pitZkAeX1w7I72TY8KTO8HXD7La%2B0pKizWBzhExqjytpmR%2BUzSTA5Ctzy3uv1nOv3gyF3UuCsDRHcEx7M2pHAAAAS3RSTlP%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwDLGfCsAAAB9ElEQVRIx72W53biMBCFhY0L7g0bTAktQEwgdMhuerbO%2Bz%2FQ2sBiY0uKcvacnX8a3Y%2FR8YyuQPDJQP8KoExcro6ZC6C4TQXQx%2FoLABV3cfozgBgL%2FAWY9ScAsR7oBCD2AmSAoD8A%2BJ3cWYECdBEaVm2z%2BU1hAuDx4fr6a08PGuuf6cmys5QvMEz0c12zhPWaAYBq9emp9%2FDlTrMUXsBOaw5Yjl5elrG%2Bu9tYAxbAtjeL%2BZ3Wdl83Ovfr3BQyYAZBoLXbHDfQ2hykTSEAAIu%2B2LRcl4tD6UCm67jPCvD4%2FON5YRhGpzOdrlar74fT5IcvOxDD0Xg0nvU7hjGVttv%2B0vYyAgyQdNgeey3Hce5DSZqN9GZmvzh8UO0F3thsiY4gqGoUtuL2AeaKpom5brVMryEKvCyXZVX0urd0wOxy4qwh8jxfLlcqZafpYoH0MzQGnNI%2F6CulOASFc%2FNWlZ17ADEG3oWjvn5TEvjbfJuyrnFaSfdyrK%2Ff1Gp1tTAHF750aqgUJUCsr5UizFUv3EeQwmOFekmVmABDCiNVlqNwOwEqcM75vp%2Bs%2FasrKpAmdxM%2FGbnfuz0j8OYnPw2v9AqZ5Nt%2Bf7hikwkw2T3Fc2l2jzdcst3DpwGCnvQ%2BEPUEu8c%2FSTSAqMfZPeX5IQK0J%2Ba%2F%2Fzn5MP4Am7ISN%2F4mSV8AAAAASUVORK5CYII%3D)](https://apt.izzysoft.de/fdroid/index/apk/fr.smarquis.sleeptimer)

| Quick Settings Tile                                                                        | Notification                                                                                               | Live Update                                                                                             |
|--------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| ![Tile](app/src/main/play/listings/en-US/graphics/phone-screenshots/1-screenshot_tile.png) | ![Notification](app/src/main/play/listings/en-US/graphics/phone-screenshots/2-screenshot_notification.png) | ![LiveUpdate](app/src/main/play/listings/en-US/graphics/phone-screenshots/3-screenshot_live_update.png) |

Sleep Timer helps you fall asleep while listening to music or podcasts.  
When the timer stops, audio playback is gradually lowered then paused.

#### Usage

1. Add the Tile in the Quick Settings panel.
2. Tap the Tile to start a timer.
3. Extend, reduce or cancel the timer with the notification actions.

__Note:__ Don't look for a launcher icon, this app only provides a Quick Settings Tile.

#### Automation

You can trigger the Sleep Timer with tools like [Tasker](https://play.google.com/store/apps/details?id=net.dinglisch.android.taskerm) or by using `adb` commands:

- Start the timer
  ```bash
  adb shell am broadcast -a fr.smarquis.sleeptimer.action.START -n fr.smarquis.sleeptimer/.SleepActionReceiver
  ```
- Start the timer with custom duration (10 min)
  ```bash
  adb shell am broadcast -a fr.smarquis.sleeptimer.action.START -n fr.smarquis.sleeptimer/.SleepActionReceiver --el extras:duration 600
  ```
- Update the timer (-1 min)
  ```bash
  adb shell am broadcast -a fr.smarquis.sleeptimer.action.UPDATE -n fr.smarquis.sleeptimer/.SleepActionReceiver --el extras:duration -60
  ```
- Stop the timer
  ```bash
  adb shell am broadcast -a fr.smarquis.sleeptimer.action.STOP -n fr.smarquis.sleeptimer/.SleepActionReceiver
  ```
- Increment (default +10 min)
  ```bash
  adb shell am broadcast -a fr.smarquis.sleeptimer.action.INCREMENT -n fr.smarquis.sleeptimer/.SleepActionReceiver
  ```
- Decrement (default -10 min)
  ```bash
  adb shell am broadcast -a fr.smarquis.sleeptimer.action.DECREMENT -n fr.smarquis.sleeptimer/.SleepActionReceiver
  ```

#### APIs

- [Tile](https://developer.android.com/reference/android/service/quicksettings/Tile.html) and [TileService](https://developer.android.com/reference/android/service/quicksettings/TileService): Quick Settings Tile
- [Notification.Builder.setTimeoutAfter(durationMs)](https://developer.android.com/reference/android/app/Notification.Builder#setTimeoutAfter(long)): set notification timeout.
- [Notification.Builder.setDeleteIntent(intent)](https://developer.android.com/reference/android/app/Notification.Builder#setDeleteIntent(android.app.PendingIntent)): set deletion action.
- [NotificationCompat.Builder.setRequestPromotedOngoing(requestPromotedOngoing)](https://developer.android.com/reference/android/app/Notification.Builder#setRequestPromotedOngoing(boolean)): request to be a promoted ongoing notification
- [AudioManager.adjustStreamVolume(STREAM_MUSIC, ADJUST_LOWER, flags)](https://developer.android.com/reference/android/media/AudioManager#adjustStreamVolume(int,%20int,%20int)): lower media volume.
- [AudioManager.setStreamVolume(STREAM_MUSIC, index, flags)](https://developer.android.com/reference/android/media/AudioManager#setStreamVolume(int,%20int,%20int)): restore initial volume.
- [AudioManager.dispatchMediaKeyEvent(KeyEvent)](https://developer.android.com/reference/android/media/AudioManager#dispatchMediaKeyEvent(android.view.KeyEvent)): sends a simulated key event for a media button.

> [!IMPORTANT]  
> Android 17 introduced [Background audio hardening](https://developer.android.com/about/versions/17/changes/bg-audio) which prevents calling `AudioManager.adjustStreamVolume(streamType, direction, flags)` from a background thread.  
> And the system does not allow `PendingIntent` used in `Notification.Builder.setDeleteIntent(intent)` to reference a foreground `Service`.  
> Therefore, an alarm must be scheduled at a precise time to trigger such foreground `Service` and pause audio playback.
> - [AlarmManager.setExactAndAllowWhileIdle(type, triggerAtMillis, PendingIntent)](https://developer.android.com/reference/android/app/AlarmManager#setExactAndAllowWhileIdle(int,%20long,%20android.app.PendingIntent)): schedule a system alarm.

# License

    Copyright 2020 Simon Marquis

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed: in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
