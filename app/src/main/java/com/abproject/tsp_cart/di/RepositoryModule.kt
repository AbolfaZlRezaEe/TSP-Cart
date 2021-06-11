package com.abproject.tsp_cart.di

import android.content.Context
import android.content.SharedPreferences
import com.abproject.tsp_cart.model.database.dao.CartDao
import com.abproject.tsp_cart.model.database.dao.ProductDao
import com.abproject.tsp_cart.model.database.dao.UserDao
import com.abproject.tsp_cart.model.repository.BaseRepository
import com.abproject.tsp_cart.model.repository.BaseRepositoryImpl
import com.abproject.tsp_cart.model.repository.admin.AdminRepository
import com.abproject.tsp_cart.model.repository.admin.AdminRepositoryImpl
import com.abproject.tsp_cart.model.repository.auth.AuthRepository
import com.abproject.tsp_cart.model.repository.auth.AuthRepositoryImpl
import com.abproject.tsp_cart.model.repository.cart.CartRepository
import com.abproject.tsp_cart.model.repository.cart.CartRepositoryImpl
import com.abproject.tsp_cart.model.repository.user.UserRepository
import com.abproject.tsp_cart.model.repository.user.UserRepositoryImpl
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
    fun provideBaseRepository(
        sharedPreferences: SharedPreferences,
        userDao: UserDao,
        @ApplicationContext context: Context,
    ): BaseRepository {
        return BaseRepositoryImpl(
            sharedPreferences,
            userDao,
            context
        )
    }

    @Provides
    @Singleton
    fun provideAdminRepository(
        productDao: ProductDao,
        baseRepository: BaseRepository,
    ): AdminRepository {
        return AdminRepositoryImpl(
            productDao,
            baseRepository
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        baseRepository: BaseRepository,
    ): AuthRepository {
        return AuthRepositoryImpl(
            userDao,
            baseRepository
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        productDao: ProductDao,
        cartDao: CartDao,
        baseRepository: BaseRepository,
    ): UserRepository {
        return UserRepositoryImpl(
            productDao,
            cartDao,
            baseRepository
        )
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartDao: CartDao,
    ): CartRepository {
        return CartRepositoryImpl(
            cartDao
        )
    }

}