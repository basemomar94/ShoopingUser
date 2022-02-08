package com.bassem.shoopinguser.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bassem.shoopinguser.ui.welcome.Welcome1
import com.bassem.shoopinguser.ui.welcome.Welcome2
import com.bassem.shoopinguser.ui.welcome.Welcome3
import com.bassem.shoopinguser.ui.welcome.Welcome4

class WelcomePageViewerAdapter(fragmentManger: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManger, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            0 -> Welcome1()
            1 -> Welcome2()
            2 -> Welcome3()
            else -> Welcome4()

        }
    }

}