package com.maks.nutrivision.data.entities

data class HomeScreenData(val deliveryCharges: String, val handlingCharges: String, val bannnerList: List<Banner> = listOf(), val categoryList: List<Category> = listOf())
