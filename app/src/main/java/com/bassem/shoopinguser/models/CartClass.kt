package com.bassem.shoopinguser.models

import android.widget.ImageView

data class CartClass(
    val title: String?=null,
    var price: String?=null,
    var numberOFItems: Int = 1,
    val photo:String?=null,
    val id :String?=null
)