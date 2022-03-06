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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class SignupFragment : Fragment(R.layout.signup_fragment) {
    var _binding: SignupFragmentBinding? = null
    val binding get() = _binding
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var mail: String
    lateinit var password: String
    lateinit var passwordCheck: String
    lateinit var name: String
    lateinit var adress: String
    lateinit var phone: String
    lateinit var userId: String
    var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        getToken()


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
        binding!!.signupButton.setOnClickListener {
            signup()
        }

    }

    fun goTo(fragment: Fragment) {
        val transcation = requireActivity().supportFragmentManager.beginTransaction()
        transcation.replace(R.id.fragmentContainerLogin, fragment)
        transcation.commit()
    }

    fun signup() {
        mail = binding!!.mail.text!!.trim().toString()
        password = binding!!.password.text!!.trim().toString()
        passwordCheck = binding!!.passwordcheck.text!!.trim().toString()
        name = binding!!.fullname.text!!.trim().toString()
        adress = binding!!.adress.text!!.trim().toString()
        phone = binding!!.phone.text!!.trim().toString()
        //Empty Check
        checkEmpty()


    }

    fun errorEmpty(text: String, layout: TextInputLayout) {
        if (text.isEmpty()) {
            layout.error = "this field can't be empty"

        } else {
            layout.isErrorEnabled = false
        }
    }

    fun checkEmpty() {
        errorEmpty(mail, binding!!.mailLayout)
        errorEmpty(password, binding!!.passwordLayout)
        errorEmpty(passwordCheck, binding!!.passwordcheckLayout)
        errorEmpty(name, binding!!.nameLayout)
        errorEmpty(adress, binding!!.adressLayout)
        errorEmpty(phone, binding!!.phoneLayout)
        if (mail.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty() && name.isNotEmpty() && adress.isNotEmpty()) {

            if (password != passwordCheck) {
                binding!!.passwordLayout.error = "Your password should be identical"
                binding!!.passwordcheckLayout.error = "Your password should be identical"
            } else {
                firebaseAuth()
            }
        }
    }

    fun firebaseAuth() {

        loading()
        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener {
            if (it.isSuccessful) {
                userId = auth.uid!!
                addtoDB(getSignupInfo(userId))
            } else {
                normal()
            }
        }.addOnFailureListener {
            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                .show()
            normal()
        }
    }

    fun gotoHome() {
        val intent = Intent(activity, HomeContainer::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    fun loading() {
        binding!!.signupButton.visibility = View.INVISIBLE
        binding!!.progressBar.visibility = View.VISIBLE
    }

    fun normal() {
        binding!!.signupButton.visibility = View.VISIBLE
        binding!!.progressBar.visibility = View.GONE
    }

    fun getSignupInfo(id: String): HashMap<String, Any> {
        val list:List<String> = arrayListOf()
        val user = HashMap<String, Any>()
        user["name"] = name
        user["id"] = id
        user["mail"] = mail
        user["address"] = adress
        user["phone"] = phone
        user["password"] = password
        user["fav"]=list
        user["cart"]=list

        auth.currentUser!!.getIdToken(true).addOnCompleteListener {
            token = it.result!!.token
        }
        user["token"] = token!!
        return user
    }

    fun addtoDB(data: HashMap<String, Any>) {
        db.collection("users").document(userId).set(data).addOnCompleteListener {
            if (it.isSuccessful) {
                gotoHome()

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

    fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                token = it.result
            }
        }
    }


}