package com.bassem.shoopinguser.ui.main_ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.FavoriteRecycleAdapter
import com.bassem.shoopinguser.databinding.FavoriteFragmentBinding
import com.bassem.shoopinguser.models.FavoriteClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FavoriteFragment : Fragment(R.layout.favorite_fragment),
    FavoriteRecycleAdapter.removeInterface {
    private lateinit var recyclerView: RecyclerView
    private lateinit var favAdapter: FavoriteRecycleAdapter
    private lateinit var favoriteList: MutableList<FavoriteClass>
    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userID: String
    private var favListIds: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteList = arrayListOf()
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomAppBar)
        recycleSetup()
        if (favoriteList.isEmpty()) {
            gettingFav()
        } else {
            favoriteList.clear()
            gettingFav()
        }


        binding!!.startShop.setOnClickListener {
            findNavController().navigate(R.id.action_Favorite_to_Home)
        }

    }


    private fun recycleSetup() {
        favAdapter = FavoriteRecycleAdapter(favoriteList, this, requireContext())
        recyclerView = requireView().findViewById(R.id.favoriteRv)
        recyclerView.apply {
            adapter = favAdapter
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)

        }

    }

    override fun remove(position: Int) {
        removeFromFav(position)
    }

    override fun addtoCart(position: Int) {
        val itemID = favoriteList[position].id
        addtoCart(itemID!!, position)

    }

    private fun gettingFav() {
        db.collection("users").document(userID).get().addOnCompleteListener { it ->
            if (it.exception != null) {
                println(it.exception!!.message)
            } else {
                favListIds = it.result!!.get("fav")


                if (favListIds != null) {
                    if ((favListIds as List<*>).isEmpty()) {
                        requireActivity().runOnUiThread {
                            hideEmptyFav()

                        }
                    } else {
                        var i = 0
                        for (item in favListIds as List<String>) {
                            db.collection("items").document(item).get().addOnSuccessListener {
                                val item = it.toObject(FavoriteClass::class.java)
                                if (item != null) {
                                    favoriteList.add(item)
                                }
                                i++
                                if (i == (favListIds as List<*>).size) {
                                    showFav()
                                }
                            }
                        }
                    }
                } else {
                    hideEmptyFav()

                }


            }
        }

    }

    private fun removeFromFav(position: Int) {
        favoriteList.removeAt(position)
        favAdapter.notifyItemRemoved(position)
        val firebaseUpdatedList: MutableList<String> = favListIds as MutableList<String>
        if (firebaseUpdatedList.isEmpty()) {
            hideEmptyFav()
        }
        firebaseUpdatedList.removeAt(position)
        db.collection("users").document(userID).update("fav", firebaseUpdatedList)
    }

    private fun addtoCart(id: String, position: Int) {
        db.collection("users").document(userID).update("cart", FieldValue.arrayUnion(id))
            .addOnCompleteListener {
                removeFromFav(position)
            }

    }

    private fun hideEmptyFav() {
        binding!!.emptyFav.visibility = View.VISIBLE
        binding!!.loadingSpinner.visibility = View.GONE
        binding!!.favoriteRv.visibility = View.GONE
    }

    private fun showFav() {
        binding!!.emptyFav.visibility = View.GONE
        binding!!.favoriteRv.visibility = View.VISIBLE
        binding!!.loadingSpinner.visibility = View.GONE
    }


}