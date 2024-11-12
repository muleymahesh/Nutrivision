package com.maks.nutrivision.data.entities

data class MyOrderResponse(
    val result: String,
    val orders: List<Order>,
)

data class Order(
    val o_id: String,
    val total: String,
    val shipping_type: String,
    val order_date: String,
    val order_status: String,
    val details: List<Detail>,
)

data class Detail(
    val qty: String,
    val product_name: String,
    val img_url: String,
    val price: String,
)