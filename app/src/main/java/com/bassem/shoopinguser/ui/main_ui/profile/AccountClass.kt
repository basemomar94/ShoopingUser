package com.bassem.shoopinguser.ui.main_ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R

class AccountClass : Fragment(R.layout.account_fragment) {
    lateinit var fabCart: CounterFab


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
        fabCart=activity!!.findViewById(R.id.cartFloating)
        fabCart.visibility=View.GONE
    }

    override fun onDetach() {
        super.onDetach()
        fabCart.visibility=View.VISIBLE
    }
}