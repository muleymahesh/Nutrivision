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
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {
    //create Product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    //read Products
    @Query(
        "SELECT p.p_id, p.img_url,p.mrp, p.product_name, p.weight, p.expiry_date,  p.long_desc, p.offer_id, p.short_desc, p.size,p.opt1_name,p.opt1_qty,p.opt1_price,p.opt2_name,p.opt2_qty,p.opt2_price,p.opt3_name,p.opt3_qty,p.opt3_price, c.selectedIndex, SUM(c.opt1_count) as opt1_count, SUM(c.opt2_count) as opt2_count, SUM(c.opt3_count) as opt3_count " +
                "FROM products p  LEFT JOIN cart c ON p.p_id = c.p_id " +
                "WHERE p.cat_id = :cat_id "+
                "GROUP BY p.product_name"
    )    fun getAllProductsByCategory(cat_id: String): Flow<List<Product>>

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    //update Product
    @Update
    suspend fun updateProduct(product: Product)

    //delete Product
    @Delete
    suspend fun deleteProduct(product: Product)

    //deleteAll
    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query("UPDATE products SET opt1_count = opt1_count + :opt1_count, opt2_count = opt2_count + :opt2_count, opt3_count = opt3_count + :opt3_count  WHERE p_id = :id")
    suspend fun updateQuantity( id:String, opt1_count:Int, opt2_count:Int, opt3_count:Int)
}
