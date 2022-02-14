package com.bassem.shoopinguser.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.databinding.SignupFragmentBinding
import com.bassem.shoopinguser.ui.main_ui.HomeContainer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignupClass : Fragment(R.layout.signup_fragment) {
    var _binding: SignupFragmentBinding? = null
    val binding get() = _binding
    lateinit var auth: FirebaseAuth

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
        binding!!.signupB.setOnClickListener {
            signup()
        }

    }

    fun goTo(fragment: Fragment) {
        val transcation = requireActivity().supportFragmentManager.beginTransaction()
        transcation.replace(R.id.fragmentContainerLogin, fragment)
        transcation.commit()
    }

    fun signup() {
        val mail = binding!!.mail.text!!.trim().toString()
        val password = binding!!.password.text!!.trim().toString()
        val passwordCheck = binding!!.passwordcheck.text!!.trim().toString()
        val name = binding!!.fullname.text!!.trim().toString()
        val adress = binding!!.adress.text!!.trim().toString()
        //Empty Check
        errorEmpty(mail, binding!!.mailLayout)
        errorEmpty(password, binding!!.passwordLayout)
        errorEmpty(passwordCheck, binding!!.passwordcheckLayout)
        errorEmpty(name, binding!!.nameLayout)
        errorEmpty(adress, binding!!.adressLayout)

        if (mail.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty() && name.isNotEmpty() && adress.isNotEmpty()) {

            if (password != passwordCheck) {
                binding!!.passwordLayout.error = "Your password should be identical"
                binding!!.passwordcheckLayout.error = "Your password should be identical"
            } else {
                loading()
                auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        gotoHome()
                    } else {
                        println(it.exception.toString())
                        //   Snackbar.make(requireView(), it.exception.toString(), Snackbar.LENGTH_LONG).show()
                        normal()
                    }
                }.addOnFailureListener {
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }


            }
        }


    }

    fun errorEmpty(text: String, layout: TextInputLayout) {
        if (text.isEmpty()) {
            layout.error = "this field can't be empty"

        } else {
            layout.isErrorEnabled = false
        }
    }

    fun gotoHome() {
        val intent = Intent(activity, HomeContainer::class.java)
        activity!!.startActivity(intent)
        activity!!.finish()
    }

    fun loading() {
        binding!!.signupB.visibility = View.GONE
        binding!!.progressBar.visibility = View.VISIBLE
    }

    fun normal() {
        binding!!.signupB.visibility = View.VISIBLE
        binding!!.progressBar.visibility = View.GONE
    }


}