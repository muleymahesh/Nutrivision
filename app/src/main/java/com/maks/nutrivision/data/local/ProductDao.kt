package com.maks.nutrivision.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.maks.nutrivision.data.entities.Product


@Dao
interface ProductDao {
    //create Product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    //read Products
    @Query("SELECT * FROM products")
    fun getAllProducts(): List<Product>

    //update Product
    @Update
    suspend fun updateProduct(product: Product)

    //delete Product
    @Delete
    suspend fun deleteProduct(product: Product)

    //deleteAll
    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query("UPDATE products SET count = count + 1 WHERE p_id = :id")
    suspend fun updateQuantity( id:String)
}