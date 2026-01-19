package com.perfecta.avivgroupassignment.data.remote.dto

import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.model.OfferType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListingDto(
    @SerialName("id")
    val id: Int,
    @SerialName("city")
    val city: String? = null,
    @SerialName("price")
    val price: Double? = null,
    @SerialName("area")
    val area: Double? = null,
    @SerialName("bedrooms")
    val bedrooms: Int? = null,
    @SerialName("rooms")
    val rooms: Int? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("professional")
    val professional: String? = null,
    @SerialName("propertyType")
    val propertyType: String? = null,
    @SerialName("offerType")
    val offerType: Int? = null
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