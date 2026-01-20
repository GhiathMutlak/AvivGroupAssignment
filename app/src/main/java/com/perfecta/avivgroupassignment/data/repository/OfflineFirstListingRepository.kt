package com.perfecta.avivgroupassignment.data.repository

import android.database.sqlite.SQLiteFullException
import com.perfecta.avivgroupassignment.data.local.dao.ListingDao
import com.perfecta.avivgroupassignment.data.local.entity.ListingEntity
import com.perfecta.avivgroupassignment.data.remote.api.ListingApiService
import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.repository.ListingRepository
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.DataError
import com.perfecta.avivgroupassignment.domain.util.EmptyResult
import com.perfecta.avivgroupassignment.domain.util.Error
import com.perfecta.avivgroupassignment.domain.util.asEmptyResult
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Offline-first repository implementation using cache-first strategy:
 * 1. Emit cached data immediately if available
 * 2. Fetch fresh data from network in background
 * 3. Update cache with new data
 * 4. Emit updated data to observers
 */
class OfflineFirstListingRepository @Inject constructor(
    private val api: ListingApiService,
    private val listingDao: ListingDao
): ListingRepository {

    /**
     * Returns a Flow that emits listings from the local database.
     * Initial load will try to refresh listings from the network if cache is empty.
     * After initial load, data comes directly from the database cache.
     */
    override suspend fun getListings(): Flow<AvivResult<List<Listing>, Error>> = flow {
        // Check if we have cached data
        val cachedListings = listingDao.getAllListings().first()

        if (cachedListings.isNotEmpty()) {
            // Emit cached data immediately (cache-first)
            emit(AvivResult.Success(cachedListings.map { it.toDomainModel() }))
        } else {
            // No cache, fetch from network
            try {
                when(val result = api.getListings()) {
                    is AvivResult.Failure -> {
                        emit(AvivResult.Failure(result.error, result.cause))
                    }
                    is AvivResult.Success -> {
                        val listings = result.data.items.map { it.toDomainModel() }
                        // Save to cache
                        listingDao.insertListings(listings.map { ListingEntity.fromDomainModel(it) })
                        emit(AvivResult.Success(listings))
                    }
                }
            } catch (e: SQLiteFullException) {
                AvivResult.Failure(DataError.Local.DISK_FULL, cause = e)
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()
                emit(AvivResult.Failure(DataError.Network.UNKNOWN, cause = e))
            }
        }
    }

    /**
     * Returns a Flow that emits listing details from the local database.
     * Falls back to network if not in cache.
     */
    override suspend fun getListingDetails(listingId: Int): Flow<AvivResult<Listing, Error>> = flow {
        // Check if we have cached data for this listing
        val cachedListing = listingDao.getListingByIdOnce(listingId)

        if (cachedListing != null) {
            // Emit cached data immediately (cache-first)
            emit(AvivResult.Success(cachedListing.toDomainModel()))
        } else {
            // No cache, fetch from network
            try {
                when(val result = api.getListingDetails(listingId)) {
                    is AvivResult.Failure -> {
                        AvivResult.Failure(result.error, result.cause)
                    }
                    is AvivResult.Success -> {
                        val listing = result.data.toDomainModel()
                        // Save to cache
                        listingDao.insertListing(ListingEntity.fromDomainModel(listing))
                        emit(AvivResult.Success(listing))
                    }
                }
            } catch (e: SQLiteFullException) {
                AvivResult.Failure(DataError.Local.DISK_FULL, cause = e)
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()
                emit(AvivResult.Failure(DataError.Network.UNKNOWN, cause = e))
            }
        }
    }

    /**
     * Force refresh listings from network and update cache.
     * Used for pull-to-refresh functionality.
     */
    override suspend fun refreshListings(): EmptyResult<Error> {
        return try {
            when (val result = api.getListings()) {
                is AvivResult.Failure -> {
                    result.asEmptyResult()
                }
                is AvivResult.Success -> {
                    val listings = result.data.items.map { it.toDomainModel() }
                    // Clear old cache and insert new data
                    listingDao.deleteAllListings()
                    listingDao.insertListings(listings.map { ListingEntity.fromDomainModel(it) })
                    AvivResult.Success(Unit)
                }
            }
        } catch (e: SQLiteFullException) {
            AvivResult.Failure(DataError.Local.DISK_FULL, cause = e)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            AvivResult.Failure(DataError.Network.UNKNOWN, cause = e)
        }
    }

    /**
     * Force refresh a specific listing from network and update cache.
     */
    override suspend fun refreshListingDetails(listingId: Int): EmptyResult<Error> {
        return try {
            when (val result = api.getListingDetails(listingId)) {
                is AvivResult.Failure -> {
                    result.asEmptyResult()
                }
                is AvivResult.Success -> {
                    val listing = result.data.toDomainModel()
                    listingDao.insertListing(ListingEntity.fromDomainModel(listing))
                    AvivResult.Success(Unit)
                }
            }
        } catch (e: SQLiteFullException) {
            AvivResult.Failure(DataError.Local.DISK_FULL, cause = e)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            AvivResult.Failure(DataError.Network.UNKNOWN, cause = e)
        }
    }
}