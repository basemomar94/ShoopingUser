package com.bassem.shoopinguser.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bassem.shoopinguser.ui.main_ui.home.HomeFragment

class ItemsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        val home = HomeFragment()
        when (position) {
            0 -> {
                val args = Bundle()
                args.putString("key", "trend")
                home.arguments = args

                return home
            }
            1 -> {
                val args = Bundle()
                args.putString("key", "novel")
                home.arguments = args

                return home
            }

            2 -> {
                val args = Bundle()
                args.putString("key", "children")
                home.arguments = args
                return home
            }

            else -> {
                val args = Bundle()
                args.putString("key", "children")
                home.arguments = args

                return home
            }
        }
    }

}