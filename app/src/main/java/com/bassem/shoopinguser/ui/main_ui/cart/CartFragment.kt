package com.bassem.shoopinguser.ui.main_ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import java.util.*
import kotlin.collections.HashMap

class CartFragment : Fragment(R.layout.cart_fragment), CartRecycleAdapter.removeInterface {
    private var _binding: CartFragmentBinding? = null
    private val binding get() = _binding
    private var cartListList: MutableList<CartClass>? = null
    private var cartAdapter: CartRecycleAdapter? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var userID: String
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var adress: String? = null
    private var phone: String? = null
    private var total: Int? = null
    private var name: String? = null
    private var cartListIds: Any? = null
    private var isCopuon = false
    private val dilveryFees = 25
    private var discount: Int = 0
    private lateinit var token: String
    lateinit var bottomNavigationView: BottomNavigationView
    private var orderID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        cartListList = arrayListOf()
        db = FirebaseFirestore.getInstance()
        getShippingInfo()


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CartFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationView.visibility = View.VISIBLE
        Log.d("Check", "onDetach")


    }


    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
        Log.d("Check", "onDestroy")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomAppBar)
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
            orderID = UUID.randomUUID().toString()

            placeOrder(getOrderDetails(orderID!!), orderID!!)
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

    private fun RecycleSetup() {
        cartAdapter = CartRecycleAdapter(cartListList!!, this@CartFragment, requireContext())
        recyclerView = requireView().findViewById(R.id.cartRv)
        recyclerView!!.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

    }

    override fun remove(position: Int, item: CartClass) {

        removeFromCart(position, item)


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

    private fun getCart() {
        db.collection("users").document(userID).get().addOnCompleteListener {
            if (it.exception != null) {
                println(it.exception!!.message)
                Toast.makeText(requireContext(), it.exception!!.message, Toast.LENGTH_LONG).show()
            } else {
                cartListIds = it.result!!.get("cart")
                if (cartListIds != null) {
                    if ((cartListIds as List<*>).isEmpty()) {
                        hideEmptycart()
                    } else {
                        var i = 0
                        for (item in cartListIds as List<String>) {
                            db.collection("items").document(item).get().addOnSuccessListener {
                                val item = it.toObject(CartClass::class.java)
                                if (item != null) {
                                    cartListList!!.add(item)
                                    cartListList?.let { it1 -> println(it1.size) }
                                    cartAdapter!!.notifyDataSetChanged()
                                    updatePrice()


                                } else {
                                    detleteAllcart()
                                }


                            }

                        }
                        showCart()

                    }
                } else {
                    hideEmptycart()
                }


            }
        }

    }

    private fun removeFromCart(position: Int, item: CartClass) {
        cartListList!!.remove(item)
        cartAdapter!!.notifyItemRemoved(position)
        var firebaseUpdatedList: MutableList<String> = cartListIds as MutableList<String>
        firebaseUpdatedList.removeAt(position)
        if (firebaseUpdatedList.isEmpty()) {
            hideEmptycart()
        }
        db.collection("users").document(userID).update("cart", firebaseUpdatedList)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updatePrice()
                }
            }

    }

    private fun updatePrice() {
        val sum = cartListList!!.sumBy { it.currentPrice!!.toInt() }
        binding!!.subtotal.text = "$sum EGP"
        total = sum + dilveryFees
        binding!!.totalCart.text = "$total EGP"
    }

    private fun hideEmptycart() {
        binding!!.cartFullLayout.visibility = View.GONE
        binding!!.emptyLayout.visibility = View.VISIBLE
        binding!!.loadingSpinner2.visibility = View.GONE
    }

    private fun showCart() {
        binding!!.cartFullLayout.visibility = View.VISIBLE
        binding!!.emptyLayout.visibility = View.GONE
        binding!!.loadingSpinner2.visibility = View.GONE
    }

    private fun getOrderDetails(orderId: String): HashMap<String, Any> {

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
        orderHashMap["cost"] = binding!!.totalCart.text.toString()
        orderHashMap["subcost"] = binding!!.subtotal.text.toString()
        val discountValue = binding!!.discountValue.text
        val discount = 0
        if (discountValue.isNullOrEmpty()) {
            orderHashMap["discount"] = discount.toString()
        } else {
            orderHashMap["discount"] = discountValue.toString()
        }

        orderHashMap["delivery"] = binding!!.delivery.text.toString()
        orderHashMap["status"] = "pending"
        orderHashMap["order_date"] = FieldValue.serverTimestamp()
        orderHashMap["order_id"] = orderId
        orderHashMap["user_id"] = userID
        orderHashMap["address"] = adress!!
        orderHashMap["phone"] = phone!!
        orderHashMap["name"] = name!!
        orderHashMap["promo"] = promo
        orderHashMap["token"] = token

        return orderHashMap

    }

    private fun placeOrder(data: HashMap<String, Any>, orderId: String) {
        loading()

        db.collection("orders").document(orderId).set(data).addOnCompleteListener {
            if (it.isSuccessful) {
                clearCart()

            } else {
                normal()
            }
        }
    }


    private fun loading() {
        binding!!.checkOut.visibility = View.GONE
        binding!!.progressBar3.visibility = View.VISIBLE
    }

    private fun normal() {
        binding!!.checkOut.visibility = View.VISIBLE
        binding!!.progressBar3.visibility = View.GONE
    }

    private fun clearCart() {
        val firebaseUpdatedList: MutableList<String> = cartListIds as MutableList<String>
        firebaseUpdatedList.clear()

        db.collection("users").document(userID).update("cart", firebaseUpdatedList)
            .addOnCompleteListener {
                showBottomSheet()
                normal()
            }

    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.confirm_bottom_sheet, null)
        dialog.setContentView(v)
        dialog.dismissWithAnimation = true
        val `continue` = dialog.findViewById<Button>(R.id.continueSheet)
        `continue`!!.setOnClickListener {
            findNavController().navigate(R.id.action_cartListClass_to_Home)
            dialog.dismiss()

        }
        val track = dialog.findViewById<Button>(R.id.trackSheet)
        track!!.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("order", orderID)
            val navHost = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navHost.navigate(R.id.action_Cart_to_tracking, bundle)
            dialog.dismiss()

        }
        dialog.setOnDismissListener {
            hideEmptycart()

        }

        dialog.show()

    }

    private fun detleteAllcart() {
        val emptlyList: List<String>
        emptlyList = arrayListOf()
        db.collection("users").document(userID).update("cart", emptlyList)
    }

    private fun getShippingInfo() {
        db.collection("users").document(userID).get().addOnSuccessListener {
            adress = it.getString("address")
            phone = it.getString("phone")
            name = it.getString("name")
            token = it.getString("token")!!

        }
    }

    private fun getCouponDiscount() {
        if (binding!!.promoLayout.text.isNotEmpty()) {
            loadingApply()
            val coupon = binding!!.promoLayout.text.toString().trim().lowercase()
            db.collection("coupons").whereEqualTo("title", coupon).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    normalApply()
                    val couponsList = it.result!!.toObjects(CouponsClass::class.java)
                    if (couponsList.isNotEmpty()) {
                        if (couponsList[0].valid!!) {
                            lockpromo()
                            discount = couponsList[0].amount!!
                            println(discount)
                            binding!!.discountValue.text = "- $discount EGP"
                            val currentSub =
                                binding!!.subtotal.text.toString().substringBefore(" EGP").toInt()
                            val discountSub = currentSub - discount
                            //    binding!!.subtotal.text = "${currentSub - discount} EGP"
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


                } else {
                    normalApply()
                }
            }

        } else {

        }


    }

    private fun loadingApply() {
        binding!!.apply.visibility = View.GONE
        binding!!.applyProgress.visibility = View.VISIBLE
    }

    private fun normalApply() {
        binding!!.apply.visibility = View.VISIBLE
        binding!!.applyProgress.visibility = View.GONE
    }

    private fun lockpromo() {
        binding!!.checkBox.isEnabled = false
        binding!!.discountLayout.visibility = View.VISIBLE
        binding!!.apply.apply {
            isEnabled = false
            alpha = .5F
        }
        binding!!.promoLayout.isEnabled = false
    }

    private fun onSwipedDelete() {
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition

                }

            }
    }


}