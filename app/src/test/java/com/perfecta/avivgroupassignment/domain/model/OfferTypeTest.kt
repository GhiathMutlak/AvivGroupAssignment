package com.perfecta.avivgroupassignment.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class OfferTypeTest {

    @Test
    fun `fromValue returns SALE for value 1`() {
        assertEquals(OfferType.SALE, OfferType.fromValue(1))
    }

    @Test
    fun `fromValue returns RENT for value 2`() {
        assertEquals(OfferType.RENT, OfferType.fromValue(2))
    }

    @Test
    fun `fromValue returns MORTGAGE for value 3`() {
        assertEquals(OfferType.MORTGAGE, OfferType.fromValue(3))
    }

    @Test
    fun `fromValue defaults to RENT for unknown value`() {
        assertEquals(OfferType.RENT, OfferType.fromValue(99))
        assertEquals(OfferType.RENT, OfferType.fromValue(-1))
        assertEquals(OfferType.RENT, OfferType.fromValue(0))
    }
}
