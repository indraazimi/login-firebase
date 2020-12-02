/*
 * Copyright (c) 2021 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 2.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainViewModel : ViewModel() {

    val authState = FirebaseUserLiveData()

    init {
        // Untuk mengirim notifikasi ke user tertentu, kita butuh token.
        // Method ini akan mengambil token yang sedang aktif saat ini.
        checkToken()
    }

    private fun checkToken() {
        val tokenTask = FirebaseMessaging.getInstance().token
        tokenTask.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "FCM token failed.", task.exception)
                return@OnCompleteListener
            }

            // Setelah mendapat token, biasanya kita mengirimkan data ini
            // ke back-end server. Tapi di praktikum ini, kita log saja.
            Log.d("FCM", "Token: ${task.result}")
        })
    }
}