package com.maks.nutrivision.data.repositories

import android.util.Log
import com.maks.nutrivision.data.entities.PlaceOrderResponse
import com.maks.nutrivision.data.entities.CartItem
import com.maks.nutrivision.data.entities.Order
import com.maks.nutrivision.data.local.CartDao
import com.maks.nutrivision.data.remote.ApiService
import com.maks.nutrivision.data.remote.PlaceOrderParams
import com.maks.nutrivision.data.remote.RequestParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OrderRepository{
    suspend fun getMyOrders(email:String): List<Order>
}
class OrderRepositoryImpl @Inject constructor( val apiService: ApiService): OrderRepository {
    override suspend fun getMyOrders(email: String): List<Order> {
        return apiService.getMyOrders(request = RequestParam(user_id = email, method = "get_order")).orders
    }


}