package com.bassem.shoopinguser.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.databinding.LoginFragmentBinding
import com.bassem.shoopinguser.ui.main_ui.HomeContainer

class LoginFragment : Fragment(R.layout.login_fragment) {
    var _binding: LoginFragmentBinding? = null
    val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.signup.setOnClickListener {
            goToSignup()
        }
        binding!!.loginBtu.setOnClickListener {
            gotoHome()
        }

    }

    fun goToSignup() {
        val transcation = requireActivity().supportFragmentManager.beginTransaction()
        transcation.replace(R.id.fragmentContainerLogin, SignupClass())
        transcation.commit()
    }

    fun gotoHome() {
        val intent = Intent(requireActivity(), HomeContainer::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }
}