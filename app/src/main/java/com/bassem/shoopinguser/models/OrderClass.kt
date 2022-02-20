package com.bassem.shoopinguser.models

import java.sql.Timestamp

data class OrderClass(
    val order_date: Timestamp? = null,
    val cost: String? = null,
    val status: String? = null,
    val order_id: String? = null,
    val user_id:String?=null
)
