package com.maks.nutrivision.data.repositories

import com.maks.nutrivision.data.entities.HomeScreenData
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.remote.ApiService
import com.maks.nutrivision.data.remote.RequestParam
import javax.inject.Inject

interface ProductRepository{
    suspend fun getProducts(cat_id: String?): List<Product>
    suspend fun getBanners(): HomeScreenData
}
class ProductRepositoryImpl @Inject constructor(
    val apiService: ApiService): ProductRepository {
    override suspend fun getProducts(cat_id: String?): List<Product> {
        if (cat_id.isNullOrEmpty())
        return apiService.getProducts(RequestParam("get_all_product")).data
        else
        return apiService.getProducts(RequestParam("get_product_by_cat",cat_id=cat_id ?: "")).data
    }

    override suspend fun getBanners(): HomeScreenData {
        val result =  apiService.getBanners(RequestParam("get_all_banner"))
        return HomeScreenData(result.data,result.new_data)
    }
}