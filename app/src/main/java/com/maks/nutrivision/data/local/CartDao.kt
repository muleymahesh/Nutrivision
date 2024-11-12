package com.maks.nutrivision.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.maks.nutrivision.data.entities.CartItem
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao {
    //create CartItem
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertCartItem(cartItem: CartItem): Long

    //read CartItems
    @Query(
        "SELECT c.id, c.p_id, p.img_url,p.mrp, p.product_name, p.weight, p.expiry_date,  p.long_desc, p.offer_id, p.short_desc, p.size, c.selectedIndex,  c.opt1_count, c.opt2_count, c.opt3_count " +
                "FROM  cart c LEFT JOIN products p ON c.p_id = p.p_id "
    )
    fun getAllCartItems(): Flow<List<CartItem>>

    //update CartItem
    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    //delete CartItem
    @Query("DELETE FROM cart WHERE p_id= :p_id AND selectedIndex = :selectedIndex")
    suspend fun deleteCartItem(p_id:String, selectedIndex: Int)

    //deleteAll
    @Query("DELETE FROM cart")
    suspend fun deleteAll()

    @Query("UPDATE cart SET opt1_count = opt1_count + 1 WHERE p_id = :id")
    suspend fun updateQuantity( id:String)

    @Query("UPDATE cart SET opt1_count = :opt1_count, opt1_count = :opt2_count, opt2_count = :opt3_count WHERE p_id = :p_id")
    suspend fun decreaseQuantity( opt1_count:Int, opt2_count:Int, opt3_count:Int, p_id: String)

    @Query("SELECT * FROM cart WHERE p_id= :id")
    suspend fun getItemById( id:String): List<CartItem>
}