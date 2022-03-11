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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.login_fragment) {
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding
    lateinit var auth: FirebaseAuth
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
            signIn()
        }

    }

    fun goToSignup() {
        val transcation = requireActivity().supportFragmentManager.beginTransaction()
        transcation.replace(R.id.fragmentContainerLogin, SignupFragment())
        transcation.commit()
    }

    fun gotoHome() {
        val intent = Intent(requireActivity(), HomeContainer::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    fun signIn() {
        val mail = binding!!.mailSignin.text!!.trim().toString()
        val password = binding!!.passSigin.text!!.trim().toString()
        SignupFragment().errorEmpty(mail, binding!!.mailSignLayout)
        SignupFragment().errorEmpty(password, binding!!.passSignLayout)
        if (mail.isNotEmpty() && password.isNotEmpty()) {
            loading()
            auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    gotoHome()
                    val userID = auth.currentUser!!.uid
                } else {
                    normal()
                    Snackbar.make(
                        requireView(),
                        it.exception!!.message.toString(),
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            }
        }

    }

    fun loading() {
        binding!!.loginBtu.visibility = View.GONE
        binding!!.progressBar2.visibility = View.VISIBLE
    }

    fun normal() {
        binding!!.loginBtu.visibility = View.VISIBLE
        binding!!.progressBar2.visibility = View.GONE
    }


}