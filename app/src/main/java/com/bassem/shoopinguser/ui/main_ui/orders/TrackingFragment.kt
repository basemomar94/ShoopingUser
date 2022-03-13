package com.bassem.shoopinguser.ui.main_ui.orders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.OrderedItemsAdapter
import com.bassem.shoopinguser.databinding.TrackingFragmentBinding
import com.bassem.shoopinguser.models.OrderedItem
import com.google.firebase.firestore.FirebaseFirestore
import com.kofigyan.stateprogressbar.StateProgressBar

class TrackingFragment : Fragment(R.layout.tracking_fragment), OrderedItemsAdapter.orderInterFace {
    private var _binding: TrackingFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderedList: MutableList<OrderedItem>
    private lateinit var orderedAdapter: OrderedItemsAdapter
    private lateinit var countList: MutableList<String>
    private lateinit var db: FirebaseFirestore
    private var orderID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderedList = arrayListOf()
        countList = arrayListOf()
        val bundle = this.arguments
        if (bundle != null) {
            orderID = bundle.getString("order")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TrackingFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleSetup()
        try {
            gettingItems()

        } catch (E: java.lang.Exception) {
            println(E.message)

        }
        binding!!.cancelOrder.setOnClickListener {
            cancelOrder()
        }


    }

    private fun tracking(status: String) {
        val track = binding!!.trackingBar
        track.apply {
            stateDescriptionData.add(0, "Pending")
            stateDescriptionData.add(1, "Confirmed")
            stateDescriptionData.add(2, "Shipped")
            stateDescriptionData.add(3, "Arrived")
            when (status) {
                "pending" -> {
                    setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
                    binding!!.cancelOrder.visibility = View.VISIBLE
                }
                "confirmed" -> {
                    setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    binding!!.cancelOrder.visibility = View.VISIBLE


                }
                "shipped" -> {
                    setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                    binding!!.cancelOrder.visibility = View.GONE

                }
                "arrived" -> {
                    setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
                    binding!!.cancelOrder.visibility = View.GONE

                }
                "canceled" -> {
                    binding!!.trackingBar.visibility = View.GONE
                }
            }


        }


    }

    private fun recycleSetup() {
        recyclerView = binding!!.trackingRV
        orderedAdapter = OrderedItemsAdapter(orderedList, requireContext(), countList, this)
        recyclerView.apply {
            adapter = orderedAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun gettingItems() {
        db = FirebaseFirestore.getInstance()
        db.collection("orders").document(orderID!!).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val total = it.result!!.get("cost")
                val subtotal = it.result!!.get("subcost")
                val discount = it.result!!.get("discount")
                Log.d("discount", (discount == null).toString())
                if (discount == null) {
                    binding!!.discountLayout.visibility = View.GONE
                } else {
                    binding!!.discount.text = "-$discount EGP"

                }
                val shipping = it.result!!.get("delivery")
                val status = it.result!!.get("status")
                val name = it.result!!.get("name")
                val address = it.result!!.get("address")
                val phone = it.result!!.get("phone")
                val numberList = it.result!!.get("count")
                try {
                    for (count in numberList as List<String>) {
                        countList.add(count)
                    }
                } catch (E: Exception) {
                    println(E.message)
                }
                tracking(status.toString())
                binding!!.orderstatus.text = status.toString()
                binding!!.total.text = total.toString() + " EGP"
                binding!!.subTotal.text = "$subtotal EGP"
                binding!!.shipping.text = "$shipping EGP"
                binding!!.trackName.text = name.toString()
                binding!!.trackAdress.text = address.toString()
                binding!!.trackPhone.text = phone.toString()
                val itemsList = it.result!!.get("items")
                if (itemsList != null) {
                    var i = 0
                    for (item in itemsList as List<*>) {
                        db.collection("items").document(item.toString()).get()
                            .addOnSuccessListener {
                                val item = it.toObject(OrderedItem::class.java)
                                orderedList.add(item!!)
                                i++
                                if (i == (itemsList as List<*>).size) {
                                    binding!!.trackLayout.visibility = View.VISIBLE
                                    binding!!.loadingSpinner3.visibility = View.GONE
                                }

                                orderedAdapter.notifyDataSetChanged()

                            }


                    }
                }

            }
        }

    }

    override fun viewItem(position: Int) {
        val id = orderedList[position].id
        val bundle = Bundle()
        bundle.putString("document", id)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        orderedList.clear()
        navController.navigate(R.id.action_tracking_to_expandView, bundle)

    }

    private fun cancelOrder() {
        cancelloading()
        db.collection("orders").document(orderID!!).update("status", "canceled")
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding!!.trackingBar.visibility = View.GONE
                    binding!!.orderstatus.text = "canceled"
                    binding!!.cancelOrder.visibility = View.GONE
                    binding!!.cancelspinner.visibility = View.GONE

                } else {
                    normalcancel()
                }
            }

    }


    private fun cancelloading() {
        binding!!.cancelOrder.visibility = View.GONE
        binding!!.cancelspinner.visibility = View.VISIBLE
    }

    private fun normalcancel() {
        binding!!.cancelOrder.visibility = View.VISIBLE
        binding!!.cancelspinner.visibility = View.GONE
    }

}
