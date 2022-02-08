package com.bassem.shoopinguser.ui.main_ui

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bassem.shoopinguser.R

class HomeContainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.title=""
        setContentView(R.layout.activity_home_container)

    }
}