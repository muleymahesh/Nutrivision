package com.maks.nutrivision.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.maks.nutrivision.data.entities.Product


@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Query("SELECT * FROM Product")
    suspend fun getAllProducts(): List<Product>?

    @Query("SELECT * from Product WHERE p_id= :id")
    fun getItemById(id: String): List<Product>?

    @Query("UPDATE Product SET count = count + 1 WHERE p_id = :id")
    suspend fun  updateQuantity(id: String)

    suspend fun insertOrUpdate( item:Product): Boolean {
        val itemsFromDB = getItemById(item.p_id)
        if (itemsFromDB != null && itemsFromDB.isEmpty())
                insert(item)
            else
                updateQuantity(item.p_id)
    return true
    }
}