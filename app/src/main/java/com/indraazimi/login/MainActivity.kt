/*
 * Copyright (c) 2021 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 2.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseUser
import com.indraazimi.login.databinding.ActivityMainBinding
import com.indraazimi.login.notify.AlarmUtils
import com.indraazimi.login.notify.FcmService
import com.indraazimi.login.notify.createChannel

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CHECK_IN_URL = "https://checkin.telkomuniversity.ac.id"
    }

    private val contract = FirebaseAuthUIActivityResultContract()
    private val signInLauncher = registerForActivityResult(contract) { }
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener { mulaiLogin() }
        binding.logout.setOnClickListener { AuthUI.getInstance().signOut(this) }
        binding.checkin.setOnClickListener { checkInSekarang() }

        viewModel.authState.observe(this, { updateUI(it) })

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

        tanganiPengumuman(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            tanganiPengumuman(intent)
        }
    }

    private fun updateUI(user: FirebaseUser?) = with(binding) {
        if (user == null) {
            userGroup.visibility = View.GONE
            login.visibility = View.VISIBLE
            AlarmUtils.setAlarmOff(this@MainActivity)
        }
        else {
            namaTextView.text = user.displayName
            Glide.with(this@MainActivity).load(user.photoUrl).into(imageView)

            userGroup.visibility = View.VISIBLE
            login.visibility = View.GONE
            AlarmUtils.setAlarm(this@MainActivity)
        }
    }

    private fun mulaiLogin() {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(intent)
    }

    private fun checkInSekarang() {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this, Uri.parse(CHECK_IN_URL))
    }

    private fun tanganiPengumuman(intent: Intent) {
        if (!intent.hasExtra(FcmService.KEY_URL)) return
        val url = intent.getStringExtra(FcmService.KEY_URL) ?: return
        val tabsIntent = CustomTabsIntent.Builder().build()
        tabsIntent.launchUrl(this, Uri.parse(url))
    }
}