package com.maks.nutrivision.data.repositories

import android.util.Log
import com.maks.nutrivision.data.entities.AuthResponse
import com.maks.nutrivision.data.entities.HomeScreenData
import com.maks.nutrivision.data.entities.PlaceOrderResponse
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.local.ProductDao
import com.maks.nutrivision.data.remote.ApiService
import com.maks.nutrivision.data.remote.PlaceOrderParams
import com.maks.nutrivision.data.remote.RequestParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CartRepository{
    suspend fun insertProduct(Product: Product)
    fun getAllProducts(): Flow<List<Product>>
    suspend fun updateProduct(Product: Product)
    suspend fun deleteProduct(Product: Product)
    suspend fun deleteAll()
    suspend fun placeOrder(placeorderParams: PlaceOrderParams): PlaceOrderResponse
}
class CartRepositoryImpl @Inject constructor(val productDao: ProductDao,val apiService: ApiService): CartRepository {
    override suspend fun insertProduct(product: Product){
       val id =  productDao.insertProduct(product)
       Log.d("TAG", "insertProduct: $id")
    }

    override fun getAllProducts()=  productDao.getAllProducts()

    override suspend fun updateProduct(product: Product) {
        productDao.updateQuantity(product.p_id)
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    override suspend fun deleteAll() {
        productDao.deleteAll()
    }
    override suspend fun placeOrder(placeorderParams: PlaceOrderParams): PlaceOrderResponse {
        return  apiService.placeOrder(placeorderParams)
    }

}