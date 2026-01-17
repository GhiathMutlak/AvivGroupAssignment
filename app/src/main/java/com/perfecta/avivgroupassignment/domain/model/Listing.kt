package com.perfecta.avivgroupassignment.domain.model

import androidx.compose.ui.text.intl.Locale

data class Listing(
    val id: Int,
    val city: String?,
    val price: Double?,
    val area: Double?,
    val bedrooms: Int?,
    val rooms: Int?,
    val imageUrl: String?,
    val professional: String?,
    val propertyType: String?,
    val offerType: OfferType?
) {
    val formattedPrice: String
        get() = "€${
            String.format(
                locale = Locale.current.platformLocale,
                format = "%,.0f",
                price ?: 0.0
            )
        }"

    val formattedArea: String
        get() = "${
            String.format(
                locale = Locale.current.platformLocale,
                format = "%.0f",
                area ?: 0
            )
        } m²"

    val bedroomsDisplay: String
        get() = bedrooms?.let { "$it bedrooms" } ?: "N/A"

    val roomsDisplay: String
        get() = rooms?.let { "$it rooms" } ?: "N/A"
}

enum class OfferType(val value: Int) {
    SALE(1),
    RENT(2),
    MORTGAGE(3);

    val displayName: String
        get() = when (this) {
            SALE -> "For Sale"
            RENT -> "For Rent"
            MORTGAGE -> "Mortgaged"
        }

    companion object {
        fun fromValue(value: Int): OfferType {
            return entries.find { it.value == value } ?: RENT
        }
    }
}
