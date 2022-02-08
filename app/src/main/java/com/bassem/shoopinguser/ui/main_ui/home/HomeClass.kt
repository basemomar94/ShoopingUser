package com.bassem.shoopinguser.ui.main_ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.HomeRecycleAdapter
import com.bassem.shoopinguser.models.ItemsClass

class HomeClass : Fragment(R.layout.home_fragment) {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter:HomeRecycleAdapter
    lateinit var itemsList: MutableList<ItemsClass>

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
        itemsList= arrayListOf()

        itemsList.add(ItemsClass("jeans",310))
        itemsList.add(ItemsClass("skirt",250))
        itemsList.add(ItemsClass("shorts",470))
        itemsList.add(ItemsClass("cap",320))
        itemsList.add(ItemsClass("shit",221))
        itemsList.add(ItemsClass("jeans",310))
        itemsList.add(ItemsClass("skirt",250))
        itemsList.add(ItemsClass("shorts",470))
        itemsList.add(ItemsClass("cap",320))
        itemsList.add(ItemsClass("shit",221))





        recycleSetup()


    }

    fun recycleSetup(){
        recyclerView=view!!.findViewById(R.id.homeRV)
        adapter= HomeRecycleAdapter(itemsList,context!!)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=GridLayoutManager(context,2)
        recyclerView.setHasFixedSize(true)
    }

}