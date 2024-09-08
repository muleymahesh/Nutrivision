package com.maks.nutrivision.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Product(
    val cat_id: String,
    val expiry_date: String,
    val img_url: String,
    val long_desc: String,
    val mrp: String,
    val offer_id: String,
    @PrimaryKey
    val p_id: String,
    val product_name: String,
    val short_desc: String,
    val size: String,
    val status: String,
    val sub_cat_id: String,
    val weight: String,
    val count: Int = 0
)