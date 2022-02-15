package com.bassem.shoopinguser.ui.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.WelcomePageViewerAdapter
import com.bassem.shoopinguser.databinding.ActivityWelcomeBinding
import com.bassem.shoopinguser.ui.login.LoginActivity
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator

class WelcomeClass : AppCompatActivity() {
    lateinit var pager: ViewPager2
    lateinit var binding: ActivityWelcomeBinding
    var currentPager: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pagerConfiguration()

        //listeners
        binding.nextBtu.setOnClickListener {
            setNextPager(currentPager!!)
        }
        binding.previousBtu.setOnClickListener {
            setPreviousPager(currentPager!!)
            println("pre")

        }
        binding.skipBtu.setOnClickListener {
            skipWelcome()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun pagerConfiguration() {
        pager = findViewById(R.id.welcomePager)
        pager.adapter = WelcomePageViewerAdapter(this.supportFragmentManager, lifecycle)
        val dotsIndicator = findViewById<SpringDotsIndicator>(R.id.dots_indicator)
        dotsIndicator.setViewPager2(pager)
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPager = position
                when (position) {
                    0 -> {
                        binding.previousBtu.visibility = View.INVISIBLE
                        binding.nextBtu.visibility = View.VISIBLE
                        binding.skipBtu.visibility = View.VISIBLE

                    }
                    1 -> {
                        binding.previousBtu.visibility = View.VISIBLE
                        binding.nextBtu.visibility = View.VISIBLE
                        binding.skipBtu.visibility = View.VISIBLE

                    }
                    2 -> {
                        binding.previousBtu.visibility = View.VISIBLE
                        binding.nextBtu.visibility = View.VISIBLE
                        binding.skipBtu.visibility = View.VISIBLE
                    }

                    3 -> {
                        binding.nextBtu.visibility = View.INVISIBLE
                        binding.previousBtu.visibility = View.VISIBLE
                        binding.skipBtu.visibility = View.INVISIBLE
                    }
                }


            }
        })
    }

    fun setNextPager(current: Int) {
        pager.currentItem = current + 1
    }

    fun setPreviousPager(current: Int) {
        pager.currentItem = current - 1
    }

    fun skipWelcome() {
        val sharedPreferences = getSharedPreferences("PREF", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("Isskip", true)
        editor.apply()
    }


}