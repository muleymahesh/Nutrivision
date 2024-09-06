package com.maks.nutrivision.data.remote

import com.maks.nutrivision.data.entities.BannerResponse
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.entities.ProductResonse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("newapi.php")
    suspend fun getProducts(@Body request: RequestParam): ProductResonse
    @POST("newapi.php")
    suspend fun getBanners(requestParam: RequestParam): BannerResponse
}