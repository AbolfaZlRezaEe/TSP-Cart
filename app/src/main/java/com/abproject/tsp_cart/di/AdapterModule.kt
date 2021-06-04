package com.abproject.tsp_cart.di

import android.app.Activity
import com.abproject.tsp_cart.view.admin.AdminAdapter
import com.abproject.tsp_cart.view.cart.CartAdapterCallBacks
import com.abproject.tsp_cart.view.user.UserAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AdapterModule {

    @Provides
    fun provideProductItemClickListener(
        activity: Activity,
    ) = activity as AdminAdapter.ProductItemClickListener

    @Provides
    fun provideProductItemFromUserClickListener(
        activity: Activity,
    ) = activity as UserAdapter.ProductItemClickListener

    @Provides
    fun provideCartClickListener(
        activity: Activity,
    ) = activity as CartAdapterCallBacks

}