package com.maks.nutrivision.ui.cart

import com.maks.nutrivision.data.entities.Banner
import com.maks.nutrivision.data.entities.Category
import com.maks.nutrivision.data.entities.Product

data class CartState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String = "",
    val deliveryCharges: Int = 0,
    val handlingCharges: Int = 0,

)