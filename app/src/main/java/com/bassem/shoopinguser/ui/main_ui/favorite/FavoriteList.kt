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

class FavoriteList : Fragment(R.layout.favorite_fragment), FavoriteRecycleAdapter.removeInterface {
    lateinit var recyclerView: RecyclerView
    lateinit var favAdapter: FavoriteRecycleAdapter
    lateinit var favoriteList: MutableList<FavoriteClass>
    var _binding: FavoriteFragmentBinding? = null
    val binding get() = _binding
    lateinit var fabCart: CounterFab
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var userID: String
    var favListIds: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteList = arrayListOf()
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
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
        fabCart = activity!!.findViewById(R.id.cartFloating)
        bottomNavigationView = activity!!.findViewById(R.id.bottomAppBar)
        recycleSetup()
        gettingFav()

        fabCart.setOnClickListener {
            findNavController().navigate(R.id.action_Favorite_to_cartListClass)
        }
        binding!!.startShop.setOnClickListener {
            findNavController().navigate(R.id.action_Favorite_to_Home)
        }

    }

    override fun onDetach() {
        super.onDetach()
        favoriteList.clear()
    }

    override fun onPause() {
        super.onPause()
        favoriteList.clear()
    }

    fun recycleSetup() {
        favAdapter = FavoriteRecycleAdapter(favoriteList, this, context!!)
        recyclerView = view!!.findViewById(R.id.favoriteRv)
        recyclerView.apply {
            adapter = favAdapter
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)

        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        fabCart.visibility = View.GONE
                        bottomNavigationView.visibility = View.GONE
                    }
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        fabCart.visibility = View.VISIBLE
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun remove(position: Int) {
        removeFromFav(position)
    }

    override fun addtoCart(position: Int) {
        val itemID = favoriteList[position].id
        addtoCart(itemID!!, position)

    }

    fun gettingFav() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).get().addOnCompleteListener { it ->
            if (it.exception != null) {
                println(it.exception!!.message)
            } else {
                Thread(kotlinx.coroutines.Runnable {


                    favListIds = it.result!!.get("fav")
                    if (favListIds != null) {
                        if ((favListIds as List<*>).isEmpty()) {
                            activity!!.runOnUiThread {
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
                                    activity!!.runOnUiThread {
                                        favAdapter.notifyDataSetChanged()
                                        i++
                                        if (i == (favListIds as List<*>).size) {
                                            showFav()
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        activity!!.runOnUiThread {
                            hideEmptyFav()

                        }
                    }


                }).start()


            }
        }

    }

    fun removeFromFav(position: Int) {
        favoriteList.removeAt(position)
        favAdapter.notifyItemRemoved(position)
        var firebaseUpdatedList: MutableList<String> = favListIds as MutableList<String>
        if (firebaseUpdatedList.isEmpty()) {
            hideEmptyFav()
        }
        firebaseUpdatedList.removeAt(position)
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).update("fav", firebaseUpdatedList)
    }

    fun addtoCart(id: String, position: Int) {
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).update("cart", FieldValue.arrayUnion(id))
            .addOnCompleteListener {
                removeFromFav(position)
            }

    }

    fun hideEmptyFav() {
        binding!!.emptyFav.visibility = View.VISIBLE
        binding!!.loadingSpinner.visibility = View.GONE
        binding!!.favoriteRv.visibility = View.GONE
    }

    fun showFav() {
        binding!!.emptyFav.visibility = View.GONE
        binding!!.favoriteRv.visibility = View.VISIBLE
        binding!!.loadingSpinner.visibility = View.GONE
    }


}