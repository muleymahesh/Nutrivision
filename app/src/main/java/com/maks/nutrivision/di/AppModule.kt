package com.maks.nutrivision.di

import android.content.Context
import androidx.room.Room
import com.maks.nutrivision.data.local.AppDatabase
import com.maks.nutrivision.data.local.CartDao
import com.maks.nutrivision.data.local.ProductDao
import com.maks.nutrivision.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "product_database"
        ).build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
    @Provides
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }
}