package com.maks.nutrivision.data.entities

data class ProductResonse(
    val data: List<Product>,
    val responseCode: Int,
    val result: String,
    val delivery_charges: Int,
    val handling_charges: Int
)