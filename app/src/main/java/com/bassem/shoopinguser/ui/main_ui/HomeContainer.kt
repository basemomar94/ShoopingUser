package com.bassem.shoopinguser.ui.main_ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeContainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar!!.title=""
        setContentView(R.layout.activity_home_container)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomAppBar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.getOrCreateBadge(R.id.Notifications).apply {
            badgeTextColor=Color.DKGRAY
            backgroundColor=Color.parseColor("#FFA56D")
            number=5
        }



    }
}