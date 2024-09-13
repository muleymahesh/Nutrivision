package com.maks.nutrivision.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Profile(
    @PrimaryKey()
    val user_id: String,
    val fname: String,
    val lname: String,
    val mobile: String,
    val order_Count: Int,
    val user_email: String
)