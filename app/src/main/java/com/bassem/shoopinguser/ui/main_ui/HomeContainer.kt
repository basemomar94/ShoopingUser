package com.bassem.shoopinguser.ui.main_ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.andremion.counterfab.CounterFab
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.adapters.HomeSliderAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderView

class HomeContainer : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var userID: String
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var fabCart: CounterFab



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_container)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        fabCart = findViewById(R.id.cartFloating)
        supportActionBar!!.title = ""
        bottomNavigationView = findViewById(R.id.bottomAppBar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)




        getFavCounter()
        getCartCounter()

    }

    fun getFavCounter() {
        db.collection("users").document(userID).addSnapshotListener { value, error ->
            if (value != null) {
                val favList = value.get("fav") as MutableList<String>
                if (favList != null) {
                    val favCount = (favList).size
                    println(favCount)
                    bottomNavigationView.getOrCreateBadge(R.id.Favorite).apply {
                        badgeTextColor = Color.DKGRAY
                        if (favCount == 0) {
                            backgroundColor = Color.parseColor("#FFFFFF")
                            clearNumber()
                            clearColorFilter()

                        } else {
                            number = favCount
                            backgroundColor = Color.parseColor("#FFA56D")


                        }
                    }
                }

            }
        }

    }

    fun getCartCounter() {
        db.collection("users").document(userID).addSnapshotListener { value, error ->
            if (value != null) {
                val cartList = value.get("cart")
                if (cartList != null) {
                    val cartCount = (cartList as List<String>).size
                    if (cartCount != null) {
                        fabCart.count = cartCount
                    }
                }

            }
        }
    }


}