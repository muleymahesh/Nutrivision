package com.maks.nutrivision.ui.order

import com.maks.nutrivision.data.entities.Product

data class PlaceOrderState(
    val isLoading: Boolean=false,
    val productList: List<Product> = listOf(),
    val address: String = "",
    val mobile: String = "",
    val name: String = "",
    val email: String = "",
    val id: String = "",
)
