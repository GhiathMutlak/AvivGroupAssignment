package com.perfecta.avivgroupassignment.data.dto

import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.model.OfferType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListingDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("city")
    val city: String?,
    @SerialName("price")
    val price: Double?,
    @SerialName("area")
    val area: Double?,
    @SerialName("bedrooms")
    val bedrooms: Int?,
    @SerialName("rooms")
    val rooms: Int?,
    @SerialName("url")
    val url: String?,
    @SerialName("professional")
    val professional: String?,
    @SerialName("propertyType")
    val propertyType: String?,
    @SerialName("offerType")
    val offerType: Int?
) {
    fun toDomainModel(): Listing {
        return Listing(
            id = id,
            city = city,
            price = price,
            area = area,
            bedrooms = bedrooms,
            rooms = rooms,
            imageUrl = url,
            professional = professional,
            propertyType = propertyType,
            offerType = OfferType.fromValue(offerType ?: 1)
        )
    }
}