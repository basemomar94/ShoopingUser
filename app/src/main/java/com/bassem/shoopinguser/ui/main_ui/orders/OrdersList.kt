package com.bassem.shoopinguser.ui.main_ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.FavoriteRecycleAdapter
import com.bassem.shoopinguser.adapters.OrdersRecycleAdapter
import com.bassem.shoopinguser.databinding.FavoriteFragmentBinding
import com.bassem.shoopinguser.databinding.OrdersFragmentBinding
import com.bassem.shoopinguser.models.FavoriteClass
import com.bassem.shoopinguser.models.OrderClass
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrdersList : Fragment(R.layout.orders_fragment),OrdersRecycleAdapter.clickInterface {
    lateinit var recyclerView: RecyclerView
    lateinit var orderAdapter: OrdersRecycleAdapter
    lateinit var orderList: MutableList<OrderClass>
    var _binding: OrdersFragmentBinding? = null
    val binding get() = _binding
    lateinit var fabCart: CounterFab
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderList = arrayListOf()
        orderList.add(OrderClass("25 March 2011","500EGP","Pending","33131"))
        orderList.add(OrderClass("25 March 2011","500EGP","Pending","33131"))
        orderList.add(OrderClass("25 March 2011","500EGP","Pending","33131"))
        orderList.add(OrderClass("25 March 2011","500EGP","Pending","33131"))
        orderList.add(OrderClass("25 March 2011","500EGP","Pending","33131"))
        orderList.add(OrderClass("25 March 2011","500EGP","Pending","33131"))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OrdersFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationView.visibility=View.VISIBLE

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabCart = activity!!.findViewById(R.id.cartFloating)
        bottomNavigationView=activity!!.findViewById(R.id.bottomAppBar)
        bottomNavigationView.visibility=View.GONE


        recycleSetup()



    }

    fun recycleSetup() {
        orderAdapter = OrdersRecycleAdapter(orderList,this)
        recyclerView = view!!.findViewById(R.id.ordersRV)
        recyclerView.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)

        }

    }

    override fun click(position: Int) {
        findNavController().navigate(R.id.action_ordersList_to_tracking)
    }

}