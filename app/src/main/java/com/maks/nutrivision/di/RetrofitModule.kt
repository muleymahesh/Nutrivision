package com.maks.nutrivision.di

import com.maks.nutrivision.data.ProductRepository
import com.maks.nutrivision.data.ProductRepositoryImpl
import com.maks.nutrivision.data.remote.ApiService
import com.maks.nutrivision.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object RetrofitModule {

    @Provides
    @ViewModelScoped
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
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
}