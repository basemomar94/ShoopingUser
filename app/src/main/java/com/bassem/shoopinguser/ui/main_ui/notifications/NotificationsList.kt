package com.bassem.shoopinguser.ui.main_ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.NotificationsRecycleAdapter
import com.bassem.shoopinguser.databinding.NotificationFragmentBinding
import com.bassem.shoopinguser.models.NotificationsClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_welcome.*

class NotificationsList : Fragment(R.layout.notification_fragment) {
    var _binding: NotificationFragmentBinding? = null
    val binding get() = _binding
    lateinit var recyclerView: RecyclerView
    lateinit var notiList: MutableList<NotificationsClass>
    lateinit var notiAdapter: NotificationsRecycleAdapter
    lateinit var fabCart: CounterFab
    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notiList = arrayListOf()
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )
        notiList.add(
            NotificationsClass(
                "offer",
                "big disscount for our grand openning dont't miss it"
            )
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotificationFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleSetup()
        fabCart = activity!!.findViewById(R.id.cartFloating)
        bottomNavigationView = activity!!.findViewById(R.id.bottomAppBar)
        fabCart.setOnClickListener {
            findNavController().navigate(R.id.action_Notifications_to_cartListClass)
        }

    }

    fun recycleSetup() {
        notiAdapter = NotificationsRecycleAdapter(notiList)
        recyclerView = view!!.findViewById(R.id.notificationsRV)
        recyclerView.apply {
            adapter = notiAdapter
            layoutManager = LinearLayoutManager(context)
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

}