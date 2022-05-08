package com.bassem.shoopinguser.ui.main_ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.HomeRecycleAdapter
import com.bassem.shoopinguser.databinding.HomeFragmentBinding
import com.bassem.shoopinguser.models.ItemsClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class HomeFragment : Fragment(R.layout.home_fragment), HomeRecycleAdapter.expandInterface,
    SearchView.OnQueryTextListener {
    private lateinit var _binding: HomeFragmentBinding
    private val binding get() = _binding
    private lateinit var recyclerView: RecyclerView
    private var adapter: HomeRecycleAdapter? = null
    private lateinit var itemsList: MutableList<ItemsClass>
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var db: FirebaseFirestore
    private lateinit var userID: String
    private lateinit var auth: FirebaseAuth
    private var favList: MutableList<String> = arrayListOf()
    private var cartList: MutableList<String> = arrayListOf()
    private var key: String? = null
    private val TAG = "HomeTag"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val args = arguments
        key = args?.getString("key")
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        db = FirebaseFirestore.getInstance()
        Log.v(TAG, "$key onCreate")


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        Log.v(TAG, "$key onCreateView")

        return binding.root


    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "$key onResume")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "$key onViewCreated")

        itemsList = arrayListOf()
        itemsList.clear()

        val search = requireActivity().findViewById<SearchView>(R.id.app_bar_search)
        search.setOnQueryTextListener(this)
        when (key) {
            "novel" -> getItemsFromFirebase("category", key!!)
            "book" -> getItemsFromFirebase("category", key!!)
            "children" -> getItemsFromFirebase("category", key!!)
            "trend" -> getItemsFromFirebase("trend", true)


        }


    }

    private fun recycleSetup() {
        view?.let {
            recyclerView = it.findViewById(R.id.trendingRv)
        }
        bottomNavigationView = requireActivity().findViewById(R.id.bottomAppBar)
        bottomNavigationView.visibility = View.VISIBLE

        adapter = HomeRecycleAdapter(itemsList, requireContext(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)


    }

    override fun makeFavorite(id: String, position: Int, fav: Boolean, item: ItemsClass) {
        val itemHere = itemsList[position]
        addtoFavorite(id)
        if (!itemHere.favorite) {
            itemHere.favorite = true

        } else {
            itemHere.favorite = false
            removeFromFav(position, id)

        }
        adapter?.notifyItemChanged(position)

    }

    private fun goToView(documentid: String, category: String, position: Int) {
        val bundle = Bundle()
        bundle.putString("document", documentid)
        bundle.putString("category", category)


        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController.navigate(R.id.action_homeClass_to_expandView, bundle)
    }

    override fun viewItem(item: String, category: String, position: Int) {
        goToView(item, category, position)
    }

    override fun addCart(id: String, position: Int, item: ItemsClass) {
        addtoCart(id)
        val itemHere = itemsList[position]
        if (!itemHere.cart) {
            itemHere.cart = true
        } else {
            itemHere.cart = false
            removeFromCart(position, id)
        }
        adapter?.notifyItemChanged(position)


    }


    private fun getItemsFromFirebase(where: String, value: Any) {
        db.collection("items").whereEqualTo(where, value).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var i = 0
                    for (dc: DocumentChange in it.result!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            itemsList.add(dc.document.toObject(ItemsClass::class.java))
//                        adapter.notifyItemInserted(i)
                        }
                        i++


                        if (i == itemsList.size) {
                            binding.trendingRv.visibility = View.VISIBLE
                            binding.shimmerLayout.visibility = View.GONE
                            getFavCounter()
                            getCartCounter()

                        }


                    }
                    recycleSetup()
                }
            }


    }

    private fun getCartCounter() {
        db.collection("users").document(userID).get().addOnCompleteListener {
            if (it.isSuccessful) {
                cartList = it.result!!.get("cart") as MutableList<String>
                itemsList.forEach { item ->
                    cartList.forEach { cart ->
                        if (item.id == cart) {
                            item.cart = true
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }

            }
        }
    }

    private fun getFavCounter() {
        db.collection("users").document(userID).get().addOnCompleteListener {
            if (it.isSuccessful) {
                favList = it.result!!.get("fav") as MutableList<String>
                itemsList.forEach { item ->
                    favList.forEach { fav ->
                        if (item.id == fav) {
                            item.favorite = true
                            adapter?.notifyDataSetChanged()
                        }

                    }

                }

            }
        }

    }

    private fun addtoFavorite(id: String) {
        db.collection("users").document(userID).update("fav", FieldValue.arrayUnion(id))
            .addOnCompleteListener {


            }

    }

    private fun addtoCart(id: String) {
        db.collection("users").document(userID).update("cart", FieldValue.arrayUnion(id))
            .addOnCompleteListener {
            }


    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        println(p0)
        filterFun(p0.toString().lowercase())
        return true
    }


    private fun filterFun(title: String) {
        val filterList: MutableList<ItemsClass> = arrayListOf()
        itemsList.forEach {
            if (it.title!!.lowercase().contains(title)) {
                filterList.add(it)
            }
        }
        adapter?.filter(filterList)

    }

    private fun removeFromFav(position: Int, item: String) {
        favList.remove(item)
        adapter?.notifyItemChanged(position)
        db.collection("users").document(userID).update("fav", FieldValue.arrayRemove(item))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                }
            }
    }

    private fun removeFromCart(position: Int, item: String) {
        cartList.remove(item)
        print(cartList.size)
        adapter?.notifyItemChanged(position)
        db.collection("users").document(userID).update("cart", FieldValue.arrayRemove(item))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                }
            }


    }




}


