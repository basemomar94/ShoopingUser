package com.bassem.shoopinguser.ui.main_ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.CartRecycleAdapter
import com.bassem.shoopinguser.databinding.CartFragmentBinding
import com.bassem.shoopinguser.models.CartClass
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartListClass : Fragment(R.layout.cart_fragment) {
    var _binding: CartFragmentBinding? = null
    val binding get() = _binding
    var cartListList: MutableList<CartClass>? = null
    var adapter: CartRecycleAdapter? = null
    var recyclerView: RecyclerView? = null
    lateinit var fabCounterFab: CounterFab
    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartListList= arrayListOf()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CartFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDetach() {
        super.onDetach()
        fabCounterFab.visibility=View.VISIBLE
        bottomNavigationView.visibility=View.VISIBLE

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartListList!!.add(CartClass("jacket",400))
        cartListList!!.add(CartClass("jacket",400))
        cartListList!!.add(CartClass("jacket",400))
        cartListList!!.add(CartClass("jacket",400))
        cartListList!!.add(CartClass("jacket",400))
        fabCounterFab=activity!!.findViewById(R.id.cartFloating)
        fabCounterFab.visibility=View.GONE
        bottomNavigationView=activity!!.findViewById(R.id.bottomAppBar)
        bottomNavigationView.visibility=View.GONE

        RecycleSetup()
    }

    fun RecycleSetup() {
        recyclerView = view!!.findViewById(R.id.cartRv)
        recyclerView!!.apply {
            adapter = CartRecycleAdapter(cartListList!!)
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

    }



}