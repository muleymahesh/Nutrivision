package com.maks.nutrivision.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maks.nutrivision.data.entities.Product

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
