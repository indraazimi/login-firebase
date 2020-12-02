/*
 * Copyright (c) 2020 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 2.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.login

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import com.indraazimi.login.notify.AlarmUtils
import com.indraazimi.login.notify.createChannel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SIGN_IN_REQUEST_CODE = 101
        private const val CHECK_IN_URL = "https://checkin.telkomuniversity.ac.id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login.setOnClickListener { mulaiLogin() }
        logout.setOnClickListener { AuthUI.getInstance().signOut(this) }
        checkin.setOnClickListener { checkInSekarang() }

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.authState.observe(this, Observer { updateUI(it) })

        // Notifikasi task sebelumnya memiliki channel "Lain-lain"
        // sehingga kurang informatif. Jadi kita buat channel baru
        // khusus untuk notifikasi dari Firebase Cloud Messaging.
        createChannel(
            this,
            R.string.news_channel_id,
            R.string.news_channel_name,
            R.string.news_channel_desc
        )

        // Agar pembuatan channel berada di satu tempat, pindahkan
        // channel sebelumnya (yang reminder) ke sini juga.
        createChannel(
            this,
            R.string.notif_channel_id,
            R.string.notif_channel_name,
            R.string.notif_channel_desc
        )
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            userGroup.visibility = View.GONE
            login.visibility = View.VISIBLE
            AlarmUtils.setAlarmOff(this)
        }
        else {
            namaTextView.text = user.displayName
            Glide.with(this).load(user.photoUrl).dontAnimate().into(imageView)

            userGroup.visibility = View.VISIBLE
            login.visibility = View.GONE
            AlarmUtils.setAlarm(this)
        }
    }

    private fun mulaiLogin() {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
    }

    private fun checkInSekarang() {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this, Uri.parse(CHECK_IN_URL))
    }
}
