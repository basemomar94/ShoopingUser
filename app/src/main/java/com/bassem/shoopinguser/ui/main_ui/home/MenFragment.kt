package com.bassem.shoopinguser.ui.main_ui.home

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.HomeRecycleAdapter
import com.bassem.shoopinguser.databinding.MenFragmentBinding
import com.bassem.shoopinguser.models.ItemsClass
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class MenFragment : Fragment(R.layout.men_fragment), HomeRecycleAdapter.expandInterface,
    SearchView.OnQueryTextListener {
    lateinit var _binding: MenFragmentBinding
    val binding get() = _binding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HomeRecycleAdapter
    lateinit var itemsList: MutableList<ItemsClass>
    lateinit var fabCart: CounterFab
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var db: FirebaseFirestore
    lateinit var userID: String
    lateinit var auth: FirebaseAuth
    var favList: MutableList<String> = arrayListOf()
    var cartList: MutableList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        db = FirebaseFirestore.getInstance()


    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menue, menu)
        val search = menu.findItem(R.id.app_bar_search)
        val SearchView = search.actionView as SearchView
        SearchView.setOnQueryTextListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MenFragmentBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabCart = requireActivity().findViewById(R.id.cartFloating)
        fabCart.visibility = View.VISIBLE

        itemsList = arrayListOf()
        recycleSetup()
        if (itemsList.isEmpty()) {
            getItemsFromFirebase()

        } else {
            itemsList.clear()
            getItemsFromFirebase()
        }

        fabCart.setOnClickListener {
            findNavController().navigate(R.id.action_homeClass_to_cartListClass)
        }


    }

    fun recycleSetup() {
        recyclerView = requireView().findViewById(R.id.menRv)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomAppBar)
        bottomNavigationView.visibility = View.VISIBLE

        adapter = HomeRecycleAdapter(itemsList, requireContext(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)

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

    override fun makeFavorite(id: String, position: Int, fav: Boolean, item: ItemsClass) {
        adapter.notifyItemChanged(position)
        val itemHere = itemsList[position]
        if (!itemHere.favorite) {
            itemHere.favorite = true
        } else {
            itemHere.favorite = false
            removeFromFav(position, id)

        }
        addtoFavorite(id)
    }

    fun goToView(documentid: String) {
        val bundle = Bundle()
        bundle.putString("document", documentid)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController.navigate(R.id.action_homeClass_to_expandView, bundle)
    }

    override fun viewItem(item: String) {
        goToView(item)
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
        adapter.notifyItemChanged(position)


    }


    fun getItemsFromFirebase() {
        db.collection("items").whereEqualTo("visible", true).whereEqualTo("category", "male").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var i = 0
                    for (dc: DocumentChange in it.result!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            itemsList.add(dc.document.toObject(ItemsClass::class.java))
                        }
                        i++
                        adapter.notifyDataSetChanged()
                        if (i == itemsList.size) {
                            binding.menRv.visibility = View.VISIBLE
                            binding.shimmerLayout.visibility = View.GONE
                            getFavCounter()
                            getCartCounter()


                        }


                    }
                }
            }

    }

    fun getCartCounter() {
        db.collection("users").document(userID).get().addOnCompleteListener {
            if (it.isSuccessful) {
                cartList = it.result!!.get("cart") as MutableList<String>
                itemsList.forEach { item ->
                    cartList.forEach { cart ->
                        if (item.id == cart) {
                            item.cart = true
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

            }
        }
    }

    fun getFavCounter() {
        db.collection("users").document(userID).get().addOnCompleteListener {
            if (it.isSuccessful) {
                favList = it.result!!.get("fav") as MutableList<String>
                itemsList.forEach { item ->
                    favList.forEach { fav ->
                        if (item.id == fav) {
                            item.favorite = true
                            adapter.notifyDataSetChanged()
                        }

                    }

                }

            }
        }

    }

    fun addtoFavorite(id: String) {
        db.collection("users").document(userID).update("fav", FieldValue.arrayUnion(id))
            .addOnCompleteListener {


            }

    }

    fun addtoCart(id: String) {
        db.collection("users").document(userID).update("cart", FieldValue.arrayUnion(id))
            .addOnCompleteListener {
            }


    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        filterFun(p0.toString().lowercase())
        return true
    }


    fun filterFun(title: String) {
        val filterList: MutableList<ItemsClass> = arrayListOf()
        itemsList.forEach {
            if (it.title!!.lowercase().contains(title)) {
                filterList.add(it)
            }
        }
        adapter.filter(filterList)

    }

    fun removeFromFav(position: Int, item: String) {
        favList.remove(item)
        adapter.notifyItemChanged(position)
        db.collection("users").document(userID).update("fav", favList).addOnCompleteListener {
            if (it.isSuccessful) {
            }
        }
    }

    private fun removeFromCart(position: Int, item: String) {
        cartList.remove(item)
        adapter.notifyItemChanged(position)
        db.collection("users").document(userID).update("cart", cartList)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                }
            }

    }


}