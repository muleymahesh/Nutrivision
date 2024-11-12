package com.maks.nutrivision.data.repositories

import android.util.Log
import com.maks.nutrivision.data.entities.PlaceOrderResponse
import com.maks.nutrivision.data.entities.CartItem
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.local.CartDao
import com.maks.nutrivision.data.remote.ApiService
import com.maks.nutrivision.data.remote.PlaceOrderParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CartRepository{
    suspend fun insertCartItem(product: Product,selectedIndex : Int)
    fun getAllCartItems(): Flow<List<CartItem>>
    suspend fun updateCartItem(cartItem: CartItem)
    suspend fun deleteCartItem(cartItem: CartItem)
    suspend fun decreaseQty(selectedIndex: Int, p_id: String)
    suspend fun deleteAll()
    suspend fun placeOrder(placeorderParams: PlaceOrderParams): PlaceOrderResponse
}
class CartRepositoryImpl @Inject constructor(val cartDao: CartDao, val apiService: ApiService): CartRepository {
    override suspend fun insertCartItem(product: Product,selectedIndex: Int){
        val list = cartDao.getItemById(product.p_id)
       val filteredList = list.filter { it.selectedIndex == selectedIndex }
        if(filteredList.isNotEmpty() ){
            val item = list.first().apply {
                when(selectedIndex){
                    0 -> opt1_count += 1
                    1 -> opt2_count += 1
                    2 -> opt3_count += 1
                }
            }
            cartDao.updateCartItem(item)

        }else{
            val item = CartItem(p_id = product.p_id, opt1_count = 0, opt2_count =0, opt3_count = 0, selectedIndex = selectedIndex).apply {
                when(selectedIndex){
                    0 -> opt1_count += 1
                    1 -> opt2_count += 1
                    2 -> opt3_count += 1
                }
            }
            val id =  cartDao.insertCartItem(item)
            Log.d("CartRepository", "insertCartItem: $id , selectedIndex $selectedIndex")
        }
    }

    override fun getAllCartItems() =  cartDao.getAllCartItems()

    override suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateQuantity(cartItem.p_id)
    }

    override suspend fun deleteCartItem(cartItem: CartItem) {
        val list = cartDao.getItemById(cartItem.p_id)
        list.firstOrNull()?.let {
            cartDao.deleteCartItem(cartItem.p_id, cartItem.selectedIndex)
        }
    }

    override suspend fun decreaseQty(selectedIndex: Int, p_id: String) {
        val list = cartDao.getItemById(p_id)
        val filteredList = list.filter { it.selectedIndex == selectedIndex }
        if(filteredList.isNotEmpty() ){
            val item = list.first().apply {
                when(selectedIndex){
                    0 -> opt1_count -= 1
                    1 -> opt2_count -= 1
                    2 -> opt3_count -= 1
                }
            }
            cartDao.updateCartItem(item)

        }
    }

    override suspend fun deleteAll() {
        cartDao.deleteAll()
    }
    override suspend fun placeOrder(placeorderParams: PlaceOrderParams): PlaceOrderResponse {
        return  apiService.placeOrder(placeorderParams)
    }

}