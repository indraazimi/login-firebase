/*
 * Copyright (c) 2021 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 2.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.login

import android.app.NotificationManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseUser
import com.indraazimi.login.databinding.ActivityMainBinding
import com.indraazimi.login.notify.sendNotification

class MainActivity : AppCompatActivity() {

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
        binding.notify.setOnClickListener { tampilNotifikasi() }

        viewModel.authState.observe(this, { updateUI(it) })
    }

    private fun updateUI(user: FirebaseUser?) = with(binding) {
        if (user == null) {
            userGroup.visibility = View.GONE
            login.visibility = View.VISIBLE
        }
        else {
            namaTextView.text = user.displayName
            Glide.with(this@MainActivity).load(user.photoUrl).into(imageView)

            userGroup.visibility = View.VISIBLE
            login.visibility = View.GONE
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

    private fun tampilNotifikasi() {
        // Seharusnya notifikasi ditampilkan oleh proses di background,
        // karena akan aneh jika notifikasi tampil saat aplikasi dibuka.
        // Kita akan menangani masalah ini nanti.
        val notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java)
        notificationManager?.sendNotification(this)
    }
}