package com.maks.nutrivision.di

import com.maks.nutrivision.data.repositories.ProductRepository
import com.maks.nutrivision.data.repositories.ProductRepositoryImpl
import com.maks.nutrivision.data.repositories.CartRepository
import com.maks.nutrivision.data.repositories.CartRepositoryImpl
import com.maks.nutrivision.data.local.ProductDao
import com.maks.nutrivision.data.local.UserDao
import com.maks.nutrivision.data.remote.ApiService
import com.maks.nutrivision.data.repositories.AuthRepository
import com.maks.nutrivision.data.repositories.AuthRepositoryImpl
import com.maks.nutrivision.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
object RetrofitModule {

    @Provides
    @ViewModelScoped
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Games
    @Provides
    @ViewModelScoped
    fun provideGameApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @ViewModelScoped
    fun provideProductRepository(gameService: ApiService): ProductRepository {
        return ProductRepositoryImpl(gameService)  // Or however you create an instance of GameRepository
    }
    @Provides
    @ViewModelScoped
    fun provideCartRepository( productDao: ProductDao): CartRepository {
        return CartRepositoryImpl(productDao)  // Or however you create an instance of GameRepository
    }

    @Provides
    @ViewModelScoped
    fun provideAuthRepository( gameService: ApiService, userDao: UserDao): AuthRepository {
        return AuthRepositoryImpl(gameService,userDao)  // Or however you create an instance of GameRepository
    }
}