package com.perfecta.avivgroupassignment.data.repository

import app.cash.turbine.test
import com.perfecta.avivgroupassignment.data.local.dao.ListingDao
import com.perfecta.avivgroupassignment.data.local.entity.ListingEntity
import com.perfecta.avivgroupassignment.data.remote.api.ListingApiService
import com.perfecta.avivgroupassignment.data.remote.dto.ListingDto
import com.perfecta.avivgroupassignment.data.remote.dto.ListingsResponse
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.DataError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OfflineFirstListingRepositoryTest {

    private lateinit var api: ListingApiService
    private lateinit var dao: ListingDao
    private lateinit var repository: OfflineFirstListingRepository

    @Before
    fun setup() {
        api = mockk()
        dao = mockk(relaxed = true)
        repository = OfflineFirstListingRepository(api, dao)
    }

    @Test
    fun `getListings returns cached data when cache is not empty`() = runTest {
        val cachedEntities = listOf(
            createListingEntity(1, "Berlin"),
            createListingEntity(2, "Hamburg")
        )
        coEvery { dao.getAllListings() } returns flowOf(cachedEntities)

        repository.getListings().test {
            val result = awaitItem()
            assertTrue(result is AvivResult.Success)
            assertEquals(2, (result as AvivResult.Success).data.size)
            assertEquals("Berlin", result.data[0].city)
            awaitComplete()
        }

        coVerify(exactly = 0) { api.getListings() }
    }

    @Test
    fun `getListings fetches from network when cache is empty`() = runTest {
        val networkResponse = ListingsResponse(
            items = listOf(createListingDto(1, "Munich")),
            totalCount = 1
        )
        coEvery { dao.getAllListings() } returns flowOf(emptyList())
        coEvery { api.getListings() } returns AvivResult.Success(networkResponse)

        repository.getListings().test {
            val result = awaitItem()
            assertTrue(result is AvivResult.Success)
            assertEquals("Munich", (result as AvivResult.Success).data[0].city)
            awaitComplete()
        }

        coVerify { dao.insertListings(any()) }
    }

    @Test
    fun `getListings returns failure when cache empty and network fails`() = runTest {
        coEvery { dao.getAllListings() } returns flowOf(emptyList())
        coEvery { api.getListings() } returns AvivResult.Failure(DataError.Network.SERVER_ERROR)

        repository.getListings().test {
            val result = awaitItem()
            assertTrue(result is AvivResult.Failure)
            assertEquals(DataError.Network.SERVER_ERROR, (result as AvivResult.Failure).error)
            awaitComplete()
        }
    }

    @Test
    fun `refreshListings clears cache and fetches fresh data`() = runTest {
        val networkResponse = ListingsResponse(
            items = listOf(createListingDto(1, "Frankfurt")),
            totalCount = 1
        )
        coEvery { api.getListings() } returns AvivResult.Success(networkResponse)

        val result = repository.refreshListings()

        assertTrue(result is AvivResult.Success)
        coVerify(exactly = 1) { dao.deleteAllListings() }
        coVerify(exactly = 1) { dao.insertListings(any()) }
    }

    private fun createListingEntity(id: Int, city: String) = ListingEntity(
        id = id,
        city = city,
        price = 100000.0,
        area = 50.0,
        bedrooms = 2,
        rooms = 3,
        imageUrl = null,
        professional = "Test Pro",
        propertyType = "Apartment",
        offerType = 1
    )

    private fun createListingDto(id: Int, city: String) = ListingDto(
        id = id,
        city = city,
        price = 100000.0,
        area = 50.0,
        bedrooms = 2,
        rooms = 3,
        url = null,
        professional = "Test Pro",
        propertyType = "Apartment",
        offerType = 1
    )
}
