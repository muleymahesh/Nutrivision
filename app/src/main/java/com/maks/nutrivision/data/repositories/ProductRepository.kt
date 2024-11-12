package com.maks.nutrivision.data.repositories

import com.maks.nutrivision.data.entities.CartItem
import com.maks.nutrivision.data.entities.HomeScreenData
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.entities.ProductResonse
import com.maks.nutrivision.data.entities.Profile
import com.maks.nutrivision.data.local.ProductDao
import com.maks.nutrivision.data.remote.ApiService
import com.maks.nutrivision.data.remote.RequestParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProductRepository{
    suspend fun getProducts(cat_id: String?): List<Product>
    suspend fun insertProducts(product: Product)
    suspend fun updateProducts(item: CartItem)
    suspend fun getProductsByIds(p_ids: String?): ProductResonse
    suspend fun getBanners(): HomeScreenData
    fun getProductsByCategory(cat_id: String): Flow<List<Product>>
}
class ProductRepositoryImpl @Inject constructor(
    val apiService: ApiService,val productDao: ProductDao): ProductRepository {
    override suspend fun getProducts(cat_id: String?): List<Product> {
        if (cat_id.isNullOrEmpty())
        return apiService.getProducts(RequestParam("get_all_product")).data
        else
        return apiService.getProducts(RequestParam("get_product_by_cat",cat_id=cat_id ?: "")).data
    }

    override suspend fun insertProducts(product: Product) {
        productDao.insertProduct(product)
    }

    override suspend fun updateProducts(item: CartItem) {
        productDao.updateQuantity(item.p_id,item.opt1_count, item.opt2_count, item.opt3_count)
    }

    override suspend fun getProductsByIds(p_ids: String?): ProductResonse {
        return apiService.getProducts(RequestParam("get_product_by_ids",p_ids=p_ids ?: ""))
    }

    override suspend fun getBanners(): HomeScreenData {
        val result =  apiService.getBanners(RequestParam("get_all_banner"))
        return HomeScreenData( deliveryCharges = "", handlingCharges = "", result.data,result.new_data)
    }

    override fun getProductsByCategory(cat_id: String): Flow<List<Product>> {

        val list =  productDao.getAllProductsByCategory(cat_id)
        return list
    }

}