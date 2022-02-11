package com.bassem.shoopinguser.ui.main_ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.FavoriteRecycleAdapter
import com.bassem.shoopinguser.databinding.FavoriteFragmentBinding
import com.bassem.shoopinguser.models.FavoriteClass

class FavoriteList : Fragment(R.layout.favorite_fragment), FavoriteRecycleAdapter.removeInterface {
    lateinit var recyclerView: RecyclerView
    lateinit var favAdapter: FavoriteRecycleAdapter
    lateinit var favoriteList: MutableList<FavoriteClass>
    var _binding: FavoriteFragmentBinding? = null
    val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteList = arrayListOf()
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
        favoriteList.add(FavoriteClass("blouse", 500))
        favoriteList.add(FavoriteClass("blouse", 500))
        favoriteList.add(FavoriteClass("blouse", 500))
        favoriteList.add(FavoriteClass("blouse", 500))
        favoriteList.add(FavoriteClass("blouse", 500))
        favoriteList.add(FavoriteClass("blouse", 500))
        recycleSetup()

    }

    fun recycleSetup() {
        favAdapter = FavoriteRecycleAdapter(favoriteList, this)
        recyclerView = view!!.findViewById(R.id.favoriteRv)
        recyclerView.apply {
            adapter = favAdapter
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)

        }
    }

    override fun remove(position: Int) {
        favoriteList.removeAt(position)
        favAdapter.notifyItemRemoved(position)
    }
}