package com.perfecta.avivgroupassignment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfecta.avivgroupassignment.data.local.entity.ListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {

    @Query("SELECT * FROM listings ORDER BY id ASC")
    fun getAllListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE id = :listingId")
    fun getListingById(listingId: Int): Flow<ListingEntity?>

    @Query("SELECT * FROM listings WHERE id = :listingId")
    suspend fun getListingByIdOnce(listingId: Int): ListingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListings(listings: List<ListingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ListingEntity)

    @Query("DELETE FROM listings")
    suspend fun deleteAllListings()

    @Query("SELECT COUNT(*) FROM listings")
    suspend fun getListingsCount(): Int
}
