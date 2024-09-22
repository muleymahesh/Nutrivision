package com.maks.nutrivision.data.remote

data class RequestParam (
    val method: String,
    val cat_id: String = "",
    val p_ids: String = "",
)
data class PlaceOrderParams(
    val amount: String,
    val city: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val method: String,
    val p_id: String,
    val phone: String,
    val price: String,
    val qty: String,
    val shipping_type: String,
    val user_id: String
)
