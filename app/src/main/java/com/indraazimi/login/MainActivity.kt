/*
 * Copyright (c) 2020 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 2.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import com.indraazimi.login.notify.AlarmUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SIGN_IN_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login.setOnClickListener { mulaiLogin() }
        logout.setOnClickListener { AuthUI.getInstance().signOut(this) }

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.authState.observe(this, Observer { updateUI(it) })
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
}
