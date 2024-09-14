package com.maks.nutrivision.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    // variable for our id.
@PrimaryKey
    val p_id: String,
    val cat_id: String?,
    val cat_name: String?,
    val expiry_date: String,
    val img_url: String,
    val long_desc: String,
    val mrp: String,
    val offer_id: String,
    val product_name: String,
    val short_desc: String,
    val size: String,
    val weight: String,
    var count: Int = 1
)