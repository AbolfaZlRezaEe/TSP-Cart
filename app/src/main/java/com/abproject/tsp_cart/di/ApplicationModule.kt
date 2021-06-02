package com.abproject.tsp_cart.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.abproject.tsp_cart.model.database.dao.ProductDao
import com.abproject.tsp_cart.model.database.TSPDataBase
import com.abproject.tsp_cart.model.database.dao.CartDao
import com.abproject.tsp_cart.model.database.dao.UserDao
import com.abproject.tsp_cart.util.Variables.DATABASE_NAME
import com.abproject.tsp_cart.util.Variables.SHARED_PREFERENCES_NAME
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
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        return context.getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideProductDoa(
        database: TSPDataBase,
    ): ProductDao = database.productDao()

    @Provides
    @Singleton
    fun provideUserDao(
        database: TSPDataBase,
    ): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideCartDao(
        database: TSPDataBase,
    ): CartDao = database.cartDao()
}