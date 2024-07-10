package com.nuvyz.app.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.nuvyz.core.utils.AddressHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocationModule {
    @Singleton
    @Provides
    fun provideGeocoder(@ApplicationContext context: Context) = Geocoder(context, Locale.getDefault())

    @Singleton
    @Provides
    fun provideAddressHelper(geocoder: Geocoder) = AddressHelper(geocoder)

    @Singleton
    @Provides
    fun provideFusedLocation(@ApplicationContext context: Context) = LocationServices.getFusedLocationProviderClient(context)


}