package com.maks.nutrivision.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    // variable for our id.
    @PrimaryKey(autoGenerate = false)
    val p_id: String,
    val cat_id: String?,
    val cat_name: String?,
    val expiry_date: String,
    val img_url: String,
    val long_desc: String,
    val mrp: String,
    val offer_id: String = "",
    val product_name: String,
    val short_desc: String,
    val size: String,
    val weight: String,
    val opt1_name: String,
    val opt1_qty: String,
    val opt1_price: String,
    val opt2_name: String,
    val opt2_qty: String,
    val opt2_price: String,
    val opt3_name: String,
    val opt3_qty: String,
    val opt3_price: String,
    var opt1_count: Int = 0,
    var opt2_count: Int = 0,
    var opt3_count: Int = 0,
    var selectedIndex: Int = 0
){
    fun getPrice(): String {
        return when (selectedIndex) {
            0 -> opt1_price
            1 -> opt2_price
            2 -> opt3_price
            else -> opt1_price
        }
    }
   fun getNamedWeight(): String {
        return when (selectedIndex) {
            0 -> opt1_name
            1 -> opt2_name
            2 -> opt3_name
            else -> opt1_name
        }
    }
    fun getSelectedCount(): Int {
        return when (selectedIndex) {
            0 -> opt1_count
            1 -> opt2_count
            2 -> opt3_count
            else -> opt1_count
        }
    }
    fun decreaseSelectedCount(item: CartItem): CartItem {
         when (selectedIndex) {
            0 -> item.opt1_count -=1
            1 -> item.opt2_count -=1
            2 -> item.opt3_count-=1
            else -> item.opt1_count-=1
        }
        return item
    }
}