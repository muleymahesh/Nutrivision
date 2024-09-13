package com.maks.nutrivision.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maks.nutrivision.data.entities.AuthResponse
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.entities.Profile

@Database(entities = [Product::class,Profile::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
}
