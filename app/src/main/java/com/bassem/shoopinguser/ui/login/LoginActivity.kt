package com.bassem.shoopinguser.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bassem.shoopinguser.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
    }
}