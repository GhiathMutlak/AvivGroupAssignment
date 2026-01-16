package com.perfecta.avivgroupassignment.domain.model

data class Listing(
    val id: Int?,
    val city: String?,
    val price: Double?,
    val area: Double?,
    val bedrooms: Int?,
    val rooms: Int?,
    val imageUrl: String?,
    val professional: String?,
    val propertyType: String?,
    val offerType: OfferType?
)

enum class OfferType(val value: Int) {
    SALE(1),
    RENT(2),
    MORTGAGE(3);

    companion object {
        fun fromValue(value: Int): OfferType {
            return entries.find { it.value == value } ?: RENT
        }
    }
}
