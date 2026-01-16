package com.perfecta.avivgroupassignment.di

import com.perfecta.avivgroupassignment.data.repository.ListingRepositoryKtor
import com.perfecta.avivgroupassignment.domain.repository.ListingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindListingRepository(listingRepository: ListingRepositoryKtor): ListingRepository
}
