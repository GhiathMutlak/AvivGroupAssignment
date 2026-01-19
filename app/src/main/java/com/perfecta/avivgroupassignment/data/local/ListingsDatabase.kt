package com.perfecta.avivgroupassignment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.perfecta.avivgroupassignment.data.local.dao.ListingDao
import com.perfecta.avivgroupassignment.data.local.entity.ListingEntity

@Database(
    entities = [ListingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ListingsDatabase : RoomDatabase() {
    abstract fun listingDao(): ListingDao
}