package com.bassem.shoopinguser.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.databinding.SignupFragmentBinding

class SignupClass : Fragment(R.layout.signup_fragment) {
    var _binding: SignupFragmentBinding? = null
    val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignupFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.login.setOnClickListener {
            goTo(LoginFragment())
        }
    }

    fun goTo(fragment: Fragment) {
        val transcation = requireActivity().supportFragmentManager.beginTransaction()
        transcation.replace(R.id.fragmentContainerLogin, fragment)
        transcation.commit()
    }
}