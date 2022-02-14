package com.bassem.shoopinguser.ui.main_ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.databinding.AccountFragmentBinding
import com.bassem.shoopinguser.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class AccountClass : Fragment(R.layout.account_fragment) {
    var _binding: AccountFragmentBinding? = null
    val binding get() = _binding
    lateinit var auth: FirebaseAuth
    lateinit var fabCart: CounterFab


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AccountFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabCart = activity!!.findViewById(R.id.cartFloating)
        fabCart.visibility = View.GONE
        //listners
        binding!!.myordersLayout.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_ordersList)
        }
        binding!!.logOut.setOnClickListener {
            logOut()
        }
    }

    override fun onDetach() {
        super.onDetach()
        fabCart.visibility = View.VISIBLE
    }

    fun logOut() {
        loading()
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        activity!!.startActivity(intent)
        activity!!.finish()
    }

    fun loading() {
        binding!!.logOut.visibility = View.GONE
        binding!!.progressBar4.visibility = View.VISIBLE
    }
}