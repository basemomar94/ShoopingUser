package com.bassem.shoopinguser.ui.main_ui.expandview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.databinding.ExpandFragmentBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ExpandViewFragment : Fragment(R.layout.expand_fragment) {
    private  var _binding: ExpandFragmentBinding? = null
    private   val binding get() = _binding
    private  lateinit var bottomNavigationView: BottomNavigationView
    private   lateinit var db: FirebaseFirestore
    private  lateinit var documentID: String
    private   lateinit var userID: String
    private  lateinit var itemID: String
    private  lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            documentID = bundle.getString("document", "fail")
        }

        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        println("IT is user $userID")


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExpandFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomAppBar)
        bottomNavigationView.visibility = View.GONE

        gettingData()
        //listners
        binding!!.cartExpand.setOnClickListener {
            if (documentID != "fail") {
                addtoCart()
            }
        }
        binding!!.checkCart.setOnClickListener {
            findNavController().navigate(R.id.action_expandView_to_cartListClass)
        }
        binding!!.favExpand.setOnClickListener {
            if (documentID != "fail") {
                addtoFavorite(documentID)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.visibility = View.GONE


    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationView.visibility = View.VISIBLE

    }

    private fun gettingData() {
        db = FirebaseFirestore.getInstance()
        db.collection("items").document(documentID).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val imageUrl = it.result!!.getString("photo")
                val title = it.result!!.getString("title")
                val price = it.result!!.getString("price")
                itemID = it.result!!.getString("id")!!
                val details = it.result!!.getString("details")
                val amount = it.result!!.getDouble("amount")
                if (amount!! > 0) {
                    itemAvaliable()

                } else {
                    itemUnavialable()
                }
                Glide.with(requireContext()).load(imageUrl).into(binding!!.itemView)
                binding!!.itemTitleview.text = title
                binding!!.itemPriceview.text = "$price EGP"
                binding!!.detailsExpand.text = details
            } else {


            }
        }
    }

    private fun addtoCart() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).update("cart", FieldValue.arrayUnion(documentID))
            .addOnCompleteListener {
                showButtonSheet()
            }

    }

    private fun showButtonSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.order_bottom_sheet, null)
        dialog.setContentView(v)
        val continine = dialog.findViewById<Button>(R.id.continueDialog)
        continine!!.setOnClickListener {
            findNavController().navigate(R.id.action_expandView_to_Home)
            dialog.dismiss()
        }
        val cart = dialog.findViewById<Button>(R.id.cartDailog)
        cart!!.setOnClickListener {
            findNavController().navigate(R.id.action_expandView_to_cartListClass)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun addtoFavorite(id: String) {
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).update("fav", FieldValue.arrayUnion(id))
            .addOnCompleteListener {
                val favorite =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.favoritered)
                binding!!.favImg.setImageDrawable(favorite)

            }

    }

    private fun itemAvaliable() {
        binding!!.progressBar5.visibility = View.GONE
        binding!!.expandLayout.visibility = View.VISIBLE
    }

    private fun itemUnavialable() {
        binding!!.progressBar5.visibility = View.GONE
        binding!!.expandLayout.visibility = View.VISIBLE
        binding!!.soldView.visibility = View.VISIBLE
        binding!!.expandLayout.alpha = .5F
        binding!!.cartExpand.isClickable = false
        binding!!.favExpand.isClickable = false
    }



}