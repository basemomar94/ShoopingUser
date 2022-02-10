package com.bassem.shoopinguser.ui.main_ui.expandview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R

class ExpandView : Fragment(R.layout.expand_fragment) {
    lateinit var fabCounterFab: CounterFab
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabCounterFab=activity!!.findViewById(R.id.cartFloating)
        fabCounterFab.visibility=View.GONE

    }

    override fun onDetach() {
        super.onDetach()
        fabCounterFab.visibility=View.VISIBLE



    }
}