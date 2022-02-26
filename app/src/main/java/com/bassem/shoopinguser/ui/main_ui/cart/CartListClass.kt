package com.bassem.shoopinguser.ui.main_ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.CartRecycleAdapter
import com.bassem.shoopinguser.databinding.CartFragmentBinding
import com.bassem.shoopinguser.models.CartClass
import com.bassem.shoopinguser.models.CouponsClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.HashMap

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
    var adress: String? = null
    var phone: String? = null
    var total: Int? = null
    var name: String? = null
    var cartListIds: Any? = null
    var isCopuon = false
    val dilveryFees = 10
    var discount: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        cartListList = arrayListOf()
        getShippingInfo()


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

        //listener
        binding!!.checkOut.setOnClickListener {
        }
        binding!!.emptyCartbutton.setOnClickListener {
            findNavController().navigate(R.id.action_cartListClass_to_Home)
        }

        binding!!.checkOut.setOnClickListener {
            placeOrder()
        }



        binding!!.checkBox.setOnClickListener {
            if (binding!!.checkBox.isChecked) {
                isCopuon = true
                binding!!.promoLayout.visibility = View.VISIBLE
                binding!!.apply.visibility = View.VISIBLE
            } else {
                binding!!.promoLayout.visibility = View.GONE
                binding!!.apply.visibility = View.GONE
                isCopuon = false

            }
        }

        binding!!.apply.setOnClickListener {
            getCouponDiscount()
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
        if (itemCart.numberOFItems < itemCart.amount!!) {
            itemCart.numberOFItems++
            itemCart.currentPrice =
                ((itemCart.numberOFItems) * (itemCart.price!!.toInt())).toString()
            val sum = cartListList!!.sumBy { it.currentPrice!!.toInt() }
            binding!!.subtotal.text = "$sum EGP"
            val discountTotal = sum - discount
            binding!!.totalCart.text = "${discountTotal + dilveryFees} EGP"

            cartAdapter!!.notifyItemChanged(position)

        } else {
            Snackbar.make(
                requireView(),
                "Sorry we only have ${itemCart.numberOFItems} of ${itemCart.title}",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun minus(position: Int) {
        val itemCart = cartListList!![position]
        if (itemCart.numberOFItems > 1) {
            itemCart.numberOFItems--
            itemCart.currentPrice =
                ((itemCart.numberOFItems) * (itemCart.price!!.toInt())).toString()
            val sum = cartListList!!.sumBy { it.currentPrice!!.toInt() }
            binding!!.subtotal.text = "$sum EGP"
            val discountTotal = sum - discount
            binding!!.totalCart.text = "${discountTotal + dilveryFees} EGP"
            cartAdapter!!.notifyItemChanged(position)


        }

    }

    fun getCart() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).get().addOnCompleteListener {
            if (it.exception != null) {
                println(it.exception!!.message)
                Toast.makeText(context!!, it.exception!!.message, Toast.LENGTH_LONG).show()
            } else {
                Thread(Runnable {

                    cartListIds = it.result!!.get("cart")
                    if (cartListIds != null) {
                        if ((cartListIds as List<*>).isEmpty()) {
                            activity!!.runOnUiThread {
                                hideEmptycart()
                            }
                        } else {
                            var i = 0
                            for (item in cartListIds as List<String>) {
                                db.collection("items").document(item).get().addOnSuccessListener {
                                    val item = it.toObject(CartClass::class.java)
                                    if (item != null) {
                                        cartListList!!.add(item)

                                    } else {
                                        detleteAllcart()
                                    }
                                    activity!!.runOnUiThread {
                                        cartAdapter!!.notifyDataSetChanged()
                                        i++
                                        if (i == (cartListIds as List<*>).size) {
                                            updatePrice()
                                            showCart()
                                        }


                                    }

                                }
                            }

                        }
                    } else {
                        activity!!.runOnUiThread {
                            hideEmptycart()
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
        if (firebaseUpdatedList.isEmpty()) {
            hideEmptycart()
        }
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).update("cart", firebaseUpdatedList)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    cartAdapter!!.notifyItemRemoved(position)
                    updatePrice()
                }
            }

    }

    fun updatePrice() {
        val sum = cartListList!!.sumBy { it.currentPrice!!.toInt() }
        binding!!.subtotal.text = "$sum EGP"
        total = sum + dilveryFees
        binding!!.totalCart.text = "$total EGP"
    }

    fun hideEmptycart() {
        binding!!.cartFullLayout.visibility = View.GONE
        binding!!.emptyLayout.visibility = View.VISIBLE
        binding!!.loadingSpinner2.visibility = View.GONE
    }

    fun showCart() {
        binding!!.cartFullLayout.visibility = View.VISIBLE
        binding!!.emptyLayout.visibility = View.GONE
        binding!!.loadingSpinner2.visibility = View.GONE
    }

    fun placeOrder() {
        loading()
        val orderID = UUID.randomUUID().toString()
        val orderHashMap = HashMap<String, Any>()
        var countList: MutableList<String>? = null
        countList = arrayListOf()
        val promo = binding!!.promoLayout.text.toString().lowercase().trim()

        cartListList!!.forEachIndexed { index, cartClass ->
            val count = cartListList!![index].numberOFItems.toString()
            println(count)
            countList.add(count)
        }
        orderHashMap["count"] = countList
        orderHashMap["items"] = cartListIds as List<String>
        orderHashMap["cost"] = binding!!.totalCart.text
        orderHashMap["status"] = "pending"
        orderHashMap["order_date"] = FieldValue.serverTimestamp()
        orderHashMap["order_id"] = orderID
        orderHashMap["user_id"] = userID
        orderHashMap["address"] = adress!!
        orderHashMap["phone"] = phone!!
        orderHashMap["name"] = name!!
        orderHashMap["promo"] = promo
        db = FirebaseFirestore.getInstance()
        db.collection("orders").document(orderID).set(orderHashMap).addOnCompleteListener {
            if (it.isSuccessful) {
                clearCart()
            } else {
                normal()
            }
        }
    }

    fun loading() {
        binding!!.checkOut.visibility = View.GONE
        binding!!.progressBar3.visibility = View.VISIBLE
    }

    fun normal() {
        binding!!.checkOut.visibility = View.VISIBLE
        binding!!.progressBar3.visibility = View.GONE
    }

    fun clearCart() {
        val firebaseUpdatedList: MutableList<String> = cartListIds as MutableList<String>
        firebaseUpdatedList.clear()

        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).update("cart", firebaseUpdatedList)
            .addOnCompleteListener {
                showBottomSheet()
                normal()
            }

    }

    fun showBottomSheet() {
        val dialog = BottomSheetDialog(context!!)
        val v = layoutInflater.inflate(R.layout.confirm_bottom_sheet, null)
        dialog.setContentView(v)
        dialog.dismissWithAnimation = true
        val contine = dialog.findViewById<Button>(R.id.continueSheet)
        contine!!.setOnClickListener {
            findNavController().navigate(R.id.action_cartListClass_to_Home)
            dialog.dismiss()

        }
        val track = dialog.findViewById<Button>(R.id.trackSheet)
        track!!.setOnClickListener {
            findNavController().navigate(R.id.action_cartListClass_to_ordersList)
            dialog.dismiss()

        }
        dialog.setOnDismissListener {
            hideEmptycart()

        }

        dialog.show()

    }

    fun detleteAllcart() {
        val emptlyList: List<String>
        emptlyList = arrayListOf()
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).update("cart", emptlyList)
    }

    fun getShippingInfo() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).get().addOnSuccessListener {
            adress = it.getString("address")
            phone = it.getString("phone")
            name = it.getString("name")

        }
    }

    fun getCouponDiscount() {
        if (binding!!.promoLayout.text.isNotEmpty()) {
            val coupon = binding!!.promoLayout.text.toString().trim().lowercase()
            db = FirebaseFirestore.getInstance()
            db.collection("coupons").whereEqualTo("title", coupon).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val couponsList = it.result!!.toObjects(CouponsClass::class.java)
                    if (couponsList.isNotEmpty()) {
                        if (couponsList[0].valid!!) {
                            lockpromo()
                            discount = couponsList[0].amount!!
                            binding!!.discountValue.text = "- $discount EGP"
                            val currentSub =
                                binding!!.subtotal.text.toString().substringBefore(" EGP").toInt()
                            val discountSub = currentSub - discount
                            binding!!.subtotal.text = "$discountSub EGP"
                            binding!!.totalCart.text = "${discountSub + dilveryFees} EGP"
                        }
                    } else {
                        binding!!.discountLayout.visibility = View.GONE
                        discount = 0

                        Toast.makeText(
                            requireContext(),
                            "Your promo code isn't valid",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
            }

        }


    }

    fun lockpromo() {
        binding!!.checkBox.isEnabled = false
        binding!!.discountLayout.visibility = View.VISIBLE
        binding!!.apply.apply {
            isEnabled = false
            alpha = .5F
        }
        binding!!.promoLayout.isEnabled = false
    }


}