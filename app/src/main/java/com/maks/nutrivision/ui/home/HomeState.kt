package com.maks.nutrivision.ui.home

import com.maks.nutrivision.data.entities.Banner
import com.maks.nutrivision.data.entities.Category
import com.maks.nutrivision.data.entities.Product

data class HomeState(
    val isLoading: Boolean = false,
    val products: List<Product?> = emptyList(),
    val categories: List<Category> = emptyList(),
    val error: String = ""

)