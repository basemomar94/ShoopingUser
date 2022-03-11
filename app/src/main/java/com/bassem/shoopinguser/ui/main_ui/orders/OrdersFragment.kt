package com.bassem.shoopinguser.ui.main_ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class OrdersFragment : Fragment(R.layout.orders_fragment), OrdersRecycleAdapter.clickInterface {
    lateinit var recyclerView: RecyclerView
    lateinit var orderAdapter: OrdersRecycleAdapter
    lateinit var orderList: MutableList<OrderClass>
    var _binding: OrdersFragmentBinding? = null
    val binding get() = _binding
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var userID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderList = arrayListOf()
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OrdersFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomAppBar)
        bottomNavigationView.visibility = View.GONE


        recycleSetup()
        if (orderList.isEmpty()) {
            getOrders()

        } else {
            orderList.clear()
            getOrders()
        }


    }

    fun recycleSetup() {
        orderAdapter = OrdersRecycleAdapter(orderList, this)
        recyclerView = requireView().findViewById(R.id.ordersRV)
        recyclerView.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)

        }

    }

    override fun click(position: Int) {
        val order = orderList[position].order_id
        val bundle = Bundle()
        bundle.putString("order", order)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController.navigate(R.id.action_ordersList_to_tracking, bundle)

    }

    fun getOrders() {
        db = FirebaseFirestore.getInstance()
        db.collection("orders").whereEqualTo("user_id", userID)
            .orderBy("order_date", Query.Direction.DESCENDING).get().addOnCompleteListener {
                println(it.exception)
                println(it.isSuccessful)
                if (it.isSuccessful) {
                    Thread(Runnable {
                        var i = 0
                        val orderCount = it.result!!.documentChanges.size
                        if (orderCount == 0) {
                            requireActivity().runOnUiThread {
                                hideOrders()

                            }
                        } else {
                            for (dc in it.result!!.documentChanges) {

                                if (dc.type == DocumentChange.Type.ADDED) {

                                    orderList.add(dc.document.toObject(OrderClass::class.java))
                                    requireActivity().runOnUiThread {
                                        i++

                                        orderAdapter.notifyDataSetChanged()
                                        println(orderList.size)
                                        if (i == orderList.size) {
                                            showOrders()
                                        }
                                    }

                                }
                            }
                        }

                    }).start()


                } else {
                }
            }


    }

    fun showOrders() {
        binding!!.ordersRV.visibility = View.VISIBLE
        binding!!.loadingSpinner4.visibility = View.GONE
    }

    fun hideOrders() {
        binding!!.loadingSpinner4.visibility = View.GONE
        binding!!.emptyOrder.visibility = View.VISIBLE

    }

}