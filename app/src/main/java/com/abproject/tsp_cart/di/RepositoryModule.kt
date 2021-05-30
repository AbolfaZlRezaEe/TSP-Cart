package com.abproject.tsp_cart.di

import android.content.Context
import android.content.SharedPreferences
import com.abproject.tsp_cart.model.database.dao.ProductDao
import com.abproject.tsp_cart.model.database.dao.UserDao
import com.abproject.tsp_cart.model.repository.AdminRepository
import com.abproject.tsp_cart.model.repository.AdminRepositoryImpl
import com.abproject.tsp_cart.model.repository.AuthRepository
import com.abproject.tsp_cart.model.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAdminRepository(
        productDao: ProductDao,
    ): AdminRepository {
        return AdminRepositoryImpl(productDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        userDao: UserDao,
        sharedPreferences: SharedPreferences,
    ): AuthRepository {
        return AuthRepositoryImpl(
            context,
            userDao,
            sharedPreferences
        )
    }
}