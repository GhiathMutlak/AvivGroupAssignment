package com.perfecta.avivgroupassignment.domain.repository

import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.Error

interface ListingRepository {
    suspend fun getListings(): AvivResult<List<Listing>, Error>
    suspend fun getListingDetails(listingId: Int): AvivResult<Listing, Error>
}