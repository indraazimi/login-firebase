/*
 * Copyright (c) 2021 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 2.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.login.notify

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

object AlarmUtils {

    private const val REQUEST_CODE = 1

    fun setAlarm(context: Context) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0)
        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        manager?.setInexactRepeating(AlarmManager.RTC_WAKEUP, getTime(),
            AlarmManager.INTERVAL_HALF_DAY, pendingIntent)
    }

    fun setAlarmOff(context: Context) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent,
            PendingIntent.FLAG_NO_CREATE)
        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        if (pendingIntent != null && manager != null) {
            manager.cancel(pendingIntent)
        }
    }

    private fun getTime(): Long {
        val calendar = Calendar.getInstance()

        // Reminder akan diberikan 2x sehari, jam 8 pagi dan 8 malam
        val jam = calendar.get(Calendar.HOUR_OF_DAY)
        val waktu = if (jam in 8..19) 20 else 8
        calendar.set(Calendar.HOUR_OF_DAY, waktu)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        // Jika jam hari ini sudah lewat, atur reminder buat besok
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 1)
        }
        return calendar.timeInMillis
    }
}