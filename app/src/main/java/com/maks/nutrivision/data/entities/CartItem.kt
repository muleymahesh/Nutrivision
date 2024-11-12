package com.maks.nutrivision.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val p_id: String,
    var opt1_count: Int,
    var opt2_count: Int,
    var opt3_count: Int,
    val selectedIndex: Int
);
