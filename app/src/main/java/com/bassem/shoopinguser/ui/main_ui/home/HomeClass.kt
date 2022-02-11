package com.bassem.shoopinguser.ui.main_ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.HomeRecycleAdapter
import com.bassem.shoopinguser.models.ItemsClass

class HomeClass : Fragment(R.layout.home_fragment), HomeRecycleAdapter.favoriteInterface,
    HomeRecycleAdapter.expandView {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HomeRecycleAdapter
    lateinit var itemsList: MutableList<ItemsClass>
    lateinit var fabCart: CounterFab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menue, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabCart = activity!!.findViewById(R.id.cartFloating)
        fabCart.count = 2
        itemsList = arrayListOf()
        itemsList.add(ItemsClass("jeans", 310, true))
        itemsList.add(ItemsClass("skirt", 250))
        itemsList.add(ItemsClass("shorts", 470))
        itemsList.add(ItemsClass("cap", 320))
        itemsList.add(ItemsClass("shit", 221, true))
        itemsList.add(ItemsClass("jeans", 310))
        itemsList.add(ItemsClass("skirt", 250))
        itemsList.add(ItemsClass("shorts", 470))
        itemsList.add(ItemsClass("cap", 320))
        itemsList.add(ItemsClass("shit", 221))
        recycleSetup()
        fabCart.setOnClickListener {
            findNavController().navigate(R.id.action_homeClass_to_cartListClass)
        }


    }

    fun recycleSetup() {
        recyclerView = view!!.findViewById(R.id.homeRV)
        adapter = HomeRecycleAdapter(itemsList, context!!, this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)



            }
        })

    }

    override fun makeFavorite(postion: Int) {
        val item = itemsList[postion]
        item.favorite = !item.favorite
        adapter.notifyItemChanged(postion)

        println(postion)

        println(itemsList.size)

    }

    fun goToView() {
        findNavController().navigate(R.id.action_homeClass_to_expandView)
    }

    override fun viewItem(position: Int) {
        goToView()
    }


}