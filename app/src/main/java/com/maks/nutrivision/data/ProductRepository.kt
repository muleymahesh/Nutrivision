package com.maks.nutrivision.data

import com.maks.nutrivision.data.entities.BannerResponse
import com.maks.nutrivision.data.entities.HomeScreenData
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.entities.ProductResonse
import com.maks.nutrivision.data.remote.ApiService
import com.maks.nutrivision.data.remote.RequestParam
import javax.inject.Inject

interface ProductRepository{
    suspend fun getProducts(): List<Product>
    suspend fun getBanners(): HomeScreenData

}
class ProductRepositoryImpl @Inject constructor(val apiService: ApiService):ProductRepository {
    override suspend fun getProducts(): List<Product> {
        return apiService.getProducts(RequestParam("get_all_product")).data
    }

    override suspend fun getBanners(): HomeScreenData {
        val result =  apiService.getBanners(RequestParam("get_all_banner"))
        return HomeScreenData(result.data,result.new_data)
    }
}