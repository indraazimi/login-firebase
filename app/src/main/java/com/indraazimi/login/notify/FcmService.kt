/*
 * Copyright (c) 2020 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 2.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.login.notify

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FcmService : FirebaseMessagingService() {

    companion object {
        const val KEY_URL = "url"
    }

    override fun onNewToken(token: String) {
        // Ini akan dipanggil setiap kali token baru di-generate.
        // Setelah mendapat token baru, biasanya kita mengirimnya
        // ke back-end server. Tapi di praktikum ini, kita log saja.
        Log.d("FCM", "Token baru: $token")
    }
}
