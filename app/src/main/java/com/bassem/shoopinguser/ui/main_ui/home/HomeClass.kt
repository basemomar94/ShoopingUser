package com.bassem.shoopinguser.ui.main_ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.bassem.shoopinguser.databinding.HomeFragmentBinding
import com.bassem.shoopinguser.models.ItemsClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class HomeClass : Fragment(R.layout.home_fragment), HomeRecycleAdapter.expandInterface,
    SearchView.OnQueryTextListener {
    lateinit var _binding: HomeFragmentBinding
    val binding get() = _binding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HomeRecycleAdapter
    lateinit var itemsList: MutableList<ItemsClass>
    lateinit var fabCart: CounterFab
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var db: FirebaseFirestore
    lateinit var userID: String
    lateinit var auth: FirebaseAuth

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
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabCart = activity!!.findViewById(R.id.cartFloating)
        fabCart.visibility = View.VISIBLE
        itemsList = arrayListOf()
        recycleSetup()
        getCartCounter()

        getItemsFromFirebase()

        fabCart.setOnClickListener {
            findNavController().navigate(R.id.action_homeClass_to_cartListClass)
        }


    }

    fun recycleSetup() {
        recyclerView = view!!.findViewById(R.id.homeRV)
        bottomNavigationView = activity!!.findViewById(R.id.bottomAppBar)
        bottomNavigationView.visibility = View.VISIBLE

        adapter = HomeRecycleAdapter(itemsList, context!!, this)
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

    override fun makeFavorite(item: String, position: Int, fav: Boolean) {
        adapter.notifyItemChanged(position)
        val itemHere = itemsList[position]
        itemHere.favorite = true
        addtoFavorite(item)
    }

    fun goToView(documentid: String) {
        val bundle = Bundle()
        bundle.putString("document", documentid)
        val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
        navController.navigate(R.id.action_homeClass_to_expandView, bundle)
    }

    override fun viewItem(item: String) {
        goToView(item)
    }

    override fun addCart(item: String, position: Int) {
        addtoCart(item)
        val itemHere = itemsList[position]
        println(itemHere.favorite)


    }

    fun getItemsFromFirebase() {
        //   db = FirebaseFirestore.getInstance()
        db.collection("items").addSnapshotListener { value, error ->
            if (error != null) {
                println(error.message)
            } else {
                var i = 0
                for (dc: DocumentChange in value!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        itemsList.add(dc.document.toObject(ItemsClass::class.java))
                    }
                    i++
                    adapter.notifyDataSetChanged()
                    if (i == itemsList.size) {
                        binding.homeRV.visibility = View.VISIBLE
                        binding.shimmerLayout.visibility = View.GONE
                        getFavCounter()


                    }


                }

            }
        }

    }

    fun getCartCounter() {
        //   db = FirebaseFirestore.getInstance()
        db.collection("users").document(userID).addSnapshotListener { value, error ->
            if (value != null) {
                val cartList = value.get("cart")
                if (cartList != null) {
                    val cartCount = (cartList as List<String>).size
                    if (cartCount != null) {
                        fabCart.count = cartCount
                    }
                }

            }
        }
    }

    fun getFavCounter() {
        db.collection("users").document(userID).addSnapshotListener { value, error ->
            if (value != null) {
                val favList = value.get("fav") as List<String>
                if (favList != null) {

                    val favCount = (favList).size
                    itemsList.forEach { item ->
                        favList.forEach { fav ->
                            if (item.id == fav) {
                                item.favorite = true
                                println(item.title)
                                adapter.notifyDataSetChanged()
                            }

                        }

                    }


                    bottomNavigationView.getOrCreateBadge(R.id.Favorite).apply {
                        badgeTextColor = Color.DKGRAY
                        if (favCount == 0) {
                            backgroundColor = Color.parseColor("#FFFFFF")
                            clearNumber()
                            clearColorFilter()

                        } else {
                            number = favCount
                            backgroundColor = Color.parseColor("#FFA56D")


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
                adapter.notifyDataSetChanged()
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


}