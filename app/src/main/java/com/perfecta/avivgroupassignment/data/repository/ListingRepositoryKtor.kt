package com.perfecta.avivgroupassignment.data.repository

import com.perfecta.avivgroupassignment.data.api.ListingApiService
import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.repository.ListingRepository
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.DataError
import com.perfecta.avivgroupassignment.domain.util.Error
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import javax.inject.Inject

class ListingRepositoryKtor @Inject constructor(
    private val api: ListingApiService
): ListingRepository {
    override suspend fun getListings(): AvivResult<List<Listing>, Error> {
        return try {
            when(val result = api.getListings()) {
                is AvivResult.Failure -> AvivResult.Failure(result.error, result.cause)
                is AvivResult.Success -> {
                    val listings = result.data.items.map { it.toDomainModel() }
                    AvivResult.Success(listings)
                }
            }
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            AvivResult.Failure(DataError.Network.UNKNOWN, cause = e)
        }
    }

    override suspend fun getListingDetails(listingId: Int): AvivResult<Listing, Error> {
        return try {
            when(val result = api.getListingDetails(listingId)) {
                is AvivResult.Failure -> AvivResult.Failure(result.error, result.cause)
                is AvivResult.Success -> {
                    AvivResult.Success(result.data.toDomainModel())
                }
            }
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            AvivResult.Failure(DataError.Network.UNKNOWN, cause = e)
        }
    }
}