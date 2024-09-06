package com.maks.nutrivision.data.entities

data class BannerResponse(
    val android_version: String,
    val data: List<Banner>,
    val ios_version: String,
    val message: String,
    val new_data: List<Category>,
    val result: String,
    val status: Int
)