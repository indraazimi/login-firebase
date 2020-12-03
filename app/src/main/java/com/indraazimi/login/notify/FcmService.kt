/*
 * Copyright (c) 2021 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 2.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.login.notify

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    companion object {
        const val KEY_URL = "url"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: return
        val body = message.notification?.body ?: return
        val url = message.data[KEY_URL] ?: return

        val notificationManager = ContextCompat.getSystemService(applicationContext,
            NotificationManager::class.java) as NotificationManager
        notificationManager.sendNotification(applicationContext, title, body, url)
    }

    override fun onNewToken(token: String) {
        // Ini akan dipanggil setiap kali token baru di-generate.
        // Setelah mendapat token baru, biasanya kita mengirimnya
        // ke back-end server. Tapi di praktikum ini, kita log saja.
        Log.d("FCM", "Token baru: $token")
    }
}