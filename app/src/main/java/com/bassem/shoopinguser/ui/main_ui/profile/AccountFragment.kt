package com.bassem.shoopinguser.ui.main_ui.profile

import android.content.Intent
import android.net.Uri
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

class AccountFragment : Fragment(R.layout.account_fragment) {
    private var _binding: AccountFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth


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
        //listners
        binding!!.myordersLayout.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_ordersList)
        }
        binding!!.logOut.setOnClickListener {
            logOut()
        }

        binding!!.wishing.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_Favorite2)
        }
        binding!!.support.setOnClickListener {
            val mobile = "1150095072"
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=+20$mobile")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            requireContext().startActivity(intent)

        }
        binding!!.share.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey Check out this Great app:"
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
        binding!!.info.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_aboutFragment)
        }
    }


    fun logOut() {
        loading()
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        requireActivity()!!.startActivity(intent)
        requireActivity()!!.finish()
    }

    fun loading() {
        binding!!.logOut.visibility = View.GONE
        binding!!.progressBar4.visibility = View.VISIBLE
    }
}