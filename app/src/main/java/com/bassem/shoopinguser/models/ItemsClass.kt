package com.bassem.shoopinguser.models

data class ItemsClass(
    val title: String? = null,
    val photo: String? = null,
    val price: String? = null,
    var favorite: Boolean = false,
    val amount: Int? = null,
    val id: String? = null,
    var cart: Boolean = false,
    var visible: Boolean? = null
)

