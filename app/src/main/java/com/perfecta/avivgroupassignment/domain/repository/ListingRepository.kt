package com.perfecta.avivgroupassignment.domain.repository

import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.EmptyResult
import com.perfecta.avivgroupassignment.domain.util.Error
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    suspend fun getListings(): Flow<AvivResult<List<Listing>, Error>>
    suspend fun getListingDetails(listingId: Int): Flow<AvivResult<Listing, Error>>
    suspend fun refreshListings(): EmptyResult<Error>
    suspend fun refreshListingDetails(listingId: Int): EmptyResult<Error>
}