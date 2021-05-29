package com.abproject.tsp_cart.di

import com.abproject.tsp_cart.model.database.ProductDao
import com.abproject.tsp_cart.model.repository.AdminRepository
import com.abproject.tsp_cart.model.repository.AdminRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}