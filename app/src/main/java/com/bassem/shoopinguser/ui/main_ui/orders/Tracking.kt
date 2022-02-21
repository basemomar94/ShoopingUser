package com.bassem.shoopinguser.ui.main_ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.OrderedItemsAdapter
import com.bassem.shoopinguser.databinding.TrackingFragmentBinding
import com.bassem.shoopinguser.models.OrderedItem
import com.google.firebase.firestore.FirebaseFirestore

class Tracking : Fragment(R.layout.tracking_fragment) {
    var _binding: TrackingFragmentBinding? = null
    val binding get() = _binding
    lateinit var recyclerView: RecyclerView
    lateinit var orderedList: MutableList<OrderedItem>
    lateinit var orderedAdapter: OrderedItemsAdapter
    lateinit var db: FirebaseFirestore
    var orderID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderedList = arrayListOf()
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
        tracking()
        recycleSetup()
        gettingItems()


    }

    fun tracking() {
        binding!!.trackingBar.stateDescriptionData.add(0, "Pending")
        binding!!.trackingBar.stateDescriptionData.add(1, "Confirmed")
        binding!!.trackingBar.stateDescriptionData.add(2, "Shipped")
        binding!!.trackingBar.stateDescriptionData.add(3, "Arrived")
    }

    fun recycleSetup() {
        recyclerView = binding!!.trackingRV
        orderedAdapter = OrderedItemsAdapter(orderedList, context!!)
        recyclerView.apply {
            adapter = orderedAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    fun gettingItems() {
        db = FirebaseFirestore.getInstance()
        db.collection("orders").document(orderID!!).get().addOnCompleteListener {
            if (it.isSuccessful) {
                Thread(Runnable {
                    val total=it.result!!.get("cost")
                    binding!!.total.text=total.toString() + " EGP"
                    binding!!.subTotal.text=(total.toString().toInt()-10).toString() + " EGP"
                    val itemsList = it.result!!.get("items")
                    if (itemsList != null) {
                        for (item in itemsList as List<*>) {
                            db.collection("items").document(item.toString()).get()
                                .addOnSuccessListener {
                                    val item = it.toObject(OrderedItem::class.java)
                                    orderedList.add(item!!)
                                    activity!!.runOnUiThread {
                                        orderedAdapter.notifyDataSetChanged()
                                    }


                                }


                        }
                    }

                }).start()

            }
        }

    }



}
