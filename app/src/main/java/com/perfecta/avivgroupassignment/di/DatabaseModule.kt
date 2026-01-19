package com.perfecta.avivgroupassignment.di

import android.content.Context
import androidx.room.Room
import com.perfecta.avivgroupassignment.data.local.ListingsDatabase
import com.perfecta.avivgroupassignment.data.local.dao.ListingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideListingsDatabase(@ApplicationContext context: Context): ListingsDatabase {
        return Room.databaseBuilder(
            context,
            ListingsDatabase::class.java,
            "real_estate_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideListingDao(database: ListingsDatabase): ListingDao {
        return database.listingDao()
    }
}