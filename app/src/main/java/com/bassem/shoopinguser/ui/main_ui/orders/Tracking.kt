package com.bassem.shoopinguser.ui.main_ui.orders

import android.os.Bundle
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

class Tracking : Fragment(R.layout.tracking_fragment), OrderedItemsAdapter.orderInterFace {
    var _binding: TrackingFragmentBinding? = null
    val binding get() = _binding
    lateinit var recyclerView: RecyclerView
    lateinit var orderedList: MutableList<OrderedItem>
    lateinit var orderedAdapter: OrderedItemsAdapter
    lateinit var countList: MutableList<String>
    lateinit var db: FirebaseFirestore
    var orderID: String? = null
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


    }

    fun tracking(status: String) {
        val track = binding!!.trackingBar
        track.apply {
            stateDescriptionData.add(0, "Pending")
            stateDescriptionData.add(1, "Confirmed")
            stateDescriptionData.add(2, "Shipped")
            stateDescriptionData.add(3, "Arrived")
            when (status) {
                "pending" -> setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
                "confirmed" -> setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                "shipped" -> setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                "arrived" -> setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
            }


        }


    }

    fun recycleSetup() {
        recyclerView = binding!!.trackingRV
        orderedAdapter = OrderedItemsAdapter(orderedList, context!!, countList, this)
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
                    val total = it.result!!.get("cost")
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
                    binding!!.total.text = total.toString() + " EGP"
                //    binding!!.subTotal.text = (total.toString().toInt() - 10).toString() + " EGP"
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
                                    activity!!.runOnUiThread {
                                        i++
                                        if (i == (itemsList as List<*>).size) {
                                            binding!!.trackLayout.visibility = View.VISIBLE
                                            binding!!.loadingSpinner3.visibility = View.GONE
                                        }
                                        /* for (count in (countList as List<String>)) {
                                             orderedList.add(OrderedItem(count = count))

                                         } */
                                        orderedAdapter.notifyDataSetChanged()


                                    }


                                }


                        }
                    }

                }).start()

            }
        }

    }

    override fun viewItem(position: Int) {
        val id = orderedList[position].id
        val bundle = Bundle()
        bundle.putString("document", id)
        val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
        orderedList.clear()
        navController.navigate(R.id.action_tracking_to_expandView, bundle)

    }


}
