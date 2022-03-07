package com.bassem.shoopinguser.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bassem.shoopinguser.ui.main_ui.home.HomeFragment
import com.bassem.shoopinguser.ui.main_ui.home.MenFragment
import com.bassem.shoopinguser.ui.main_ui.home.WomenFragment

class ItemsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> MenFragment()

            else -> {
                WomenFragment()
            }
        }
    }

}