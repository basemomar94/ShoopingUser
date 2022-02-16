package com.bassem.shoopinguser.ui.main_ui.expandview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.databinding.ExpandFragmentBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class ExpandView : Fragment(R.layout.expand_fragment) {
    var _binding: ExpandFragmentBinding? = null
    val binding get() = _binding
    lateinit var fabCounterFab: CounterFab
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var db: FirebaseFirestore
    lateinit var documentID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            documentID = bundle.getString("document", "")
        }


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
        fabCounterFab = activity!!.findViewById(R.id.cartFloating)
        fabCounterFab.visibility = View.GONE
        bottomNavigationView = activity!!.findViewById(R.id.bottomAppBar)
        bottomNavigationView.visibility = View.GONE
        gettingData()

    }

    override fun onResume() {
        super.onResume()
        fabCounterFab.visibility = View.GONE
        bottomNavigationView.visibility = View.GONE


    }

    override fun onDetach() {
        super.onDetach()
        fabCounterFab.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE

    }

    fun gettingData() {
        db = FirebaseFirestore.getInstance()
        db.collection("items").document(documentID).addSnapshotListener { value, error ->
            if (error != null) {
                println(error.message)
            } else {

                val imageUrl = value!!.getString("photo")
                val title = value.getString("title")
                val price = value.getString("price")
                Glide.with(context!!).load(imageUrl).into(binding!!.itemView)
                binding!!.itemTitleview.text = title
                binding!!.itemPriceview.text = price + " EGP"
            }
        }
    }


}