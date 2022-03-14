package com.bassem.shoopinguser.ui.main_ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.HomeSliderAdapter
import com.bassem.shoopinguser.adapters.ItemsPagerAdapter
import com.bassem.shoopinguser.databinding.HomePageContainerBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderView

class HomePageContainer : Fragment(R.layout.home_page_container) {
    var _binding: HomePageContainerBinding? = null
    val binding get() = this._binding
    lateinit var pager: ViewPager2
    lateinit var pagerAdapter: ItemsPagerAdapter
    lateinit var homeslider: SliderView
    lateinit var sliderHomeAdapter: HomeSliderAdapter
    lateinit var tab: TabLayout
    val tabtitles = arrayOf("Trending", "Books", "Novels","Children")
    var imageList = listOf<Int>(
        R.drawable.welcome1,
        R.drawable.welcome2,
        R.drawable.welcome3,
        R.drawable.welcome4
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this._binding = HomePageContainerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sliderSetup()
        pagerSetup()
    }

    fun pagerSetup() {
        tab = requireView().findViewById(R.id.tabs)
        pager = requireView().findViewById(R.id.homePager)
        pagerAdapter =
            ItemsPagerAdapter(requireActivity().supportFragmentManager, requireActivity().lifecycle)
        pager.adapter = pagerAdapter
        TabLayoutMediator(tab, pager) {

                tab, position ->
            tab.text = tabtitles[position]

        }.attach()
    }

    fun sliderSetup() {
        homeslider = requireView().findViewById(R.id.homeSlider)
        sliderHomeAdapter = HomeSliderAdapter(imageList)
        homeslider.apply {
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            autoCycleDirection=2
            scrollTimeInSec=2
            setSliderAdapter(sliderHomeAdapter)
            startAutoCycle()
        }


    }
}