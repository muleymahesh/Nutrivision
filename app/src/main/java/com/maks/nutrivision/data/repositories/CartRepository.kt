package com.maks.nutrivision.data.repositories

import android.util.Log
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.local.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CartRepository{
    suspend fun insertProduct(Product: Product)
    suspend fun getAllProducts(): List<Product>
    suspend fun updateProduct(Product: Product)
    suspend fun deleteProduct(Product: Product)
    suspend fun deleteAll()
}
class CartRepositoryImpl @Inject constructor(val productDao: ProductDao): CartRepository {
    override suspend fun insertProduct(product: Product){
       val id =  productDao.insertProduct(product)
       Log.d("TAG", "insertProduct: $id")
    }

    override suspend fun getAllProducts()= withContext(Dispatchers.IO){
         productDao.getAllProducts()
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateQuantity(product.p_id)
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    override suspend fun deleteAll() {
        productDao.deleteAll()
    }
}