package com.abproject.tsp_cart.di

import android.content.Context
import androidx.room.Room
import com.abproject.tsp_cart.model.database.ProductDao
import com.abproject.tsp_cart.model.database.TSPDataBase
import com.abproject.tsp_cart.util.Variables.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideProductDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context,
        TSPDataBase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideProductDoa(
        database: TSPDataBase,
    ): ProductDao = database.productDao()
}