package com.bassem.shoopinguser.ui.main_ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.CartRecycleAdapter
import com.bassem.shoopinguser.databinding.CartFragmentBinding
import com.bassem.shoopinguser.models.CartClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class CartListClass : Fragment(R.layout.cart_fragment), CartRecycleAdapter.removeInterface {
    var _binding: CartFragmentBinding? = null
    val binding get() = _binding
    var cartListList: MutableList<CartClass>? = null
    var cartAdapter: CartRecycleAdapter? = null
    var recyclerView: RecyclerView? = null
    lateinit var fabCounterFab: CounterFab
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var userID: String
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    var cartListIds: Any? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        cartListList = arrayListOf()


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
        fabCounterFab.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabCounterFab = activity!!.findViewById(R.id.cartFloating)
        fabCounterFab.visibility = View.GONE
        bottomNavigationView = activity!!.findViewById(R.id.bottomAppBar)
        bottomNavigationView.visibility = View.GONE
        RecycleSetup()
        getCart()
        binding!!.checkOut.setOnClickListener {
        }
        binding!!.emptyCartbutton.setOnClickListener {
            findNavController().navigate(R.id.action_cartListClass_to_Home)
        }



        binding!!.checkBox.setOnClickListener {
            if (binding!!.checkBox.isChecked) {
                binding!!.promoLayout.visibility = View.VISIBLE
            } else {
                binding!!.promoLayout.visibility = View.GONE

            }
        }

    }

    fun RecycleSetup() {
        cartAdapter = CartRecycleAdapter(cartListList!!, this@CartListClass, context!!)
        recyclerView = view!!.findViewById(R.id.cartRv)
        recyclerView!!.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

    }

    override fun remove(position: Int) {

        removeFromCart(position)


    }

    override fun add(position: Int) {
        val itemCart = cartListList!![position]
        if (itemCart.numberOFItems < 10) {
            itemCart.numberOFItems++
            println(itemCart.numberOFItems)
            updatePrice()

        }
        cartAdapter!!.notifyItemChanged(position)
    }

    override fun minus(position: Int) {
        val itemCart = cartListList!![position]
        if (itemCart.numberOFItems > 1) {
            itemCart.numberOFItems--
            updatePrice()

        }
        cartAdapter!!.notifyItemChanged(position)

    }

    fun getCart() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).get().addOnCompleteListener {
            if (it.exception != null) {
                println(it.exception!!.message)
            } else {
                Thread(Runnable {

                    cartListIds = it.result!!.get("cart")
                    if (cartListIds != null) {
                        for (item in cartListIds as List<String>) {
                            db.collection("items").document(item).get().addOnSuccessListener {
                                val item = it.toObject(CartClass::class.java)
                                if (item != null) {
                                    cartListList!!.add(item)
                                }
                                activity!!.runOnUiThread {
                                    cartAdapter!!.notifyDataSetChanged()
                                    hideEmptycart()
                                    updatePrice()
                                    println("Inside ui")
                                }
                            }
                        }
                    }

                }).start()


            }
        }

    }

    fun removeFromCart(position: Int) {
        cartListList!!.removeAt(position)
        var firebaseUpdatedList: MutableList<String> = cartListIds as MutableList<String>
        firebaseUpdatedList.removeAt(position)
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).update("cart", firebaseUpdatedList)
        cartAdapter!!.notifyItemRemoved(position)
        updatePrice()
        hideEmptycart()

    }

    fun updatePrice() {
        val sum = cartListList!!.sumBy { it.price!!.toInt() }
        binding!!.subtotal.text = "$sum EGP"
        val dilveryFees = 25
        val total = sum + dilveryFees
        binding!!.totalCart.text = "$total EGP"
        println("$sum================$total")
    }

    fun hideEmptycart() {
        if (cartListList!!.isEmpty()) {
            binding!!.cartFullLayout.visibility = View.GONE
            binding!!.emptyLayout.visibility = View.VISIBLE
        } else {
            binding!!.cartFullLayout.visibility = View.VISIBLE
            binding!!.emptyLayout.visibility = View.GONE
        }
    }


}