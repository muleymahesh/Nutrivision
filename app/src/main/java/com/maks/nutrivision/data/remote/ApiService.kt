package com.maks.nutrivision.data.remote

import com.maks.nutrivision.data.entities.AuthResponse
import com.maks.nutrivision.data.entities.BannerResponse
import com.maks.nutrivision.data.entities.LoginRequest
import com.maks.nutrivision.data.entities.MyOrderResponse
import com.maks.nutrivision.data.entities.PlaceOrderResponse
import com.maks.nutrivision.data.entities.ProductResonse
import com.maks.nutrivision.data.entities.SignupRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("newapi.php")
    suspend fun getProducts(@Body request: RequestParam): ProductResonse

    @POST("newapi.php")
    suspend fun getProductsByIds(@Body request: RequestParam): ProductResonse
    @POST("newapi.php")
    suspend fun getBanners(@Body requestParam: RequestParam): BannerResponse

    @POST("newapi.php")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("newapi.php")
    suspend fun signup(@Body request: SignupRequest): AuthResponse

    @POST("newapi.php")
    suspend fun placeOrder(@Body request: PlaceOrderParams): PlaceOrderResponse

    @POST("newapi.php")
    suspend fun getMyOrders(@Body request: RequestParam): MyOrderResponse
}