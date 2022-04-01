package com.bassem.shoopinguser.ui.main_ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.databinding.MyinfoFragmentBinding
import com.bassem.shoopinguser.models.UserClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class YourinfoFragment : Fragment(R.layout.myinfo_fragment) {
    var binding: MyinfoFragmentBinding? = null
    var auth: FirebaseAuth? = null
    var db: FirebaseFirestore? = null
    var userId: String? = null
    var user = MutableLiveData<UserClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userId = auth?.currentUser?.uid
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyinfoFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserInfo()

        //Observers
        user.observe(viewLifecycleOwner) {
            if (it != null) {
                upadteUi(it)
            }

        }
        //listners
        binding?.updateButton?.setOnClickListener {
            loading(true)
            updateMyinfo()
        }
    }


    private fun getUserInfo() {
        db?.collection("users")?.document(userId!!)?.get()?.addOnCompleteListener {
            if (it.isSuccessful) {

                val userinfo = it.result?.toObject(UserClass::class.java)
                user.postValue(userinfo!!)


            }
        }

    }

    private fun upadteUi(user: UserClass) {
        binding?.apply {
            fullname.setText(user.name)
            mail.setText(user.mail)
            phone.setText(user.phone)
            adress.setText(user.address)
        }

    }

    private fun updateMyinfo() {
        val user: MutableMap<String, String> = mutableMapOf()
        user["name"] = binding?.fullname?.text.toString()
        user["mail"] = binding?.mail?.text.toString()
        user["phone"] = binding?.phone?.text.toString()
        user["address"] = binding?.adress?.text.toString()
        db?.collection("users")?.document(userId!!)?.update(user as Map<String, Any>)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "your information has been updated",
                        Toast.LENGTH_SHORT
                    ).show()
                    loading(false)

                } else {
                    loading(false)
                    Toast.makeText(
                        requireContext(),
                        it.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun loading(isloading: Boolean) {
        if (isloading) {
            binding?.apply {
                updateButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        } else {
            binding?.apply {
                updateButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }


    }
}