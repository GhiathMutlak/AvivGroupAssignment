package com.perfecta.avivgroupassignment.data.local

import com.perfecta.avivgroupassignment.data.local.entity.ListingEntity
import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.model.OfferType
import org.junit.Assert.assertEquals
import org.junit.Test

class ListingEntityMapperTest {

    @Test
    fun `toDomainModel maps all fields correctly`() {
        val entity = ListingEntity(
            id = 1,
            city = "Berlin",
            price = 250000.0,
            area = 85.5,
            bedrooms = 2,
            rooms = 4,
            imageUrl = "https://example.com/image.jpg",
            professional = "Immowelt",
            propertyType = "Apartment",
            offerType = 1
        )

        val result = entity.toDomainModel()

        assertEquals(1, result.id)
        assertEquals("Berlin", result.city)
        assertEquals(250000.0, result.price)
        assertEquals(85.5, result.area)
        assertEquals(2, result.bedrooms)
        assertEquals(4, result.rooms)
        assertEquals("https://example.com/image.jpg", result.imageUrl)
        assertEquals("Immowelt", result.professional)
        assertEquals("Apartment", result.propertyType)
        assertEquals(OfferType.SALE, result.offerType)
    }

    @Test
    fun `fromDomainModel and toDomainModel are reversible`() {
        val original = Listing(
            id = 42,
            city = "Hamburg",
            price = 180000.0,
            area = 65.0,
            bedrooms = 1,
            rooms = 3,
            imageUrl = "https://example.com/apt.jpg",
            professional = "HomeDay",
            propertyType = "Studio",
            offerType = OfferType.RENT
        )

        val entity = ListingEntity.fromDomainModel(original)
        val result = entity.toDomainModel()

        assertEquals(original, result)
    }

    @Test
    fun `toDomainModel defaults to RENT when offerType is null`() {
        val entity = ListingEntity(
            id = 1,
            city = "Munich",
            price = null,
            area = null,
            bedrooms = null,
            rooms = null,
            imageUrl = null,
            professional = null,
            propertyType = null,
            offerType = null
        )

        val result = entity.toDomainModel()

        assertEquals(OfferType.RENT, result.offerType)
    }
}
