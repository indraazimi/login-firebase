/*
 * Copyright (c) 2021 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 2.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.login.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.indraazimi.login.MainActivity
import com.indraazimi.login.R

private const val NOTIFICATION_ID = 0
private const val PENGUMUMAN_ID = 1

fun NotificationManager.sendNotification(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        PendingIntent.getActivity(context, NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.notif_channel_id)
    )
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentTitle(context.getString(R.string.notif_title))
        .setContentText(context.getString(R.string.notif_message))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.sendNotification(context: Context,
                                         title: String, body: String, url: String) {
    val intent = Intent(context, MainActivity::class.java)
    intent.putExtra(FcmService.KEY_URL, url)
    val pendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        PendingIntent.getActivity(context, PENGUMUMAN_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getActivity(context, PENGUMUMAN_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.news_channel_id)
    )
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentTitle(title)
        .setContentText(body)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(PENGUMUMAN_ID, builder.build())
}

fun createChannel(context: Context, idRes: Int, nameRes: Int, descRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            context.getString(idRes),
            context.getString(nameRes),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(false)
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = context.getString(descRes)
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(notificationChannel)
    }
}