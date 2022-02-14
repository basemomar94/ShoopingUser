package com.bassem.shoopinguser.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.databinding.ActivityWelcomeBinding
import com.bassem.shoopinguser.databinding.SplashFragmentBinding
import com.bassem.shoopinguser.ui.login.LoginActivity
import com.bassem.shoopinguser.ui.main_ui.HomeContainer
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    lateinit var binding: SplashFragmentBinding
    lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        isLogin()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = SplashFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun isSkip() {
        val sharedPreferences = getSharedPreferences("PREF", Context.MODE_PRIVATE)
        val isSkipped = sharedPreferences.getBoolean("Isskip", false)
        if (isSkipped) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            val intent = Intent(this, WelcomeClass::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun isLogin() {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomeContainer::class.java)
            startActivity(intent)
            finish()
        } else {

            isSkip()
        }
    }
}