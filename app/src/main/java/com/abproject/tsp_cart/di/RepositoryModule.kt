package com.abproject.tsp_cart.di

import android.content.Context
import android.content.SharedPreferences
import com.abproject.tsp_cart.model.database.dao.CartDao
import com.abproject.tsp_cart.model.database.dao.ProductDao
import com.abproject.tsp_cart.model.database.dao.UserDao
import com.abproject.tsp_cart.model.repository.*
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
        sharedPreferences: SharedPreferences,
    ): AdminRepository {
        return AdminRepositoryImpl(
            productDao,
            sharedPreferences
        )
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

    @Provides
    @Singleton
    fun provideUserRepository(
        cartDao: CartDao,
        productDao: ProductDao,
        sharedPreferences: SharedPreferences,
    ): UserRepository {
        return UserRepositoryImpl(
            cartDao,
            productDao,
            sharedPreferences
        )
    }
}