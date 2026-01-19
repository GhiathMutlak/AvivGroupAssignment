package com.perfecta.avivgroupassignment.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.model.OfferType

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey
    val id: Int,
    val city: String?,
    val price: Double?,
    val area: Double?,
    val bedrooms: Int?,
    val rooms: Int?,
    val imageUrl: String?,
    val professional: String?,
    val propertyType: String?,
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
            imageUrl = imageUrl,
            professional = professional,
            propertyType = propertyType,
            offerType = OfferType.fromValue(offerType ?: OfferType.RENT.value)
        )
    }

    companion object {
        fun fromDomainModel(listing: Listing): ListingEntity {
            return ListingEntity(
                id = listing.id,
                city = listing.city,
                price = listing.price,
                area = listing.area,
                bedrooms = listing.bedrooms,
                rooms = listing.rooms,
                imageUrl = listing.imageUrl,
                professional = listing.professional,
                propertyType = listing.propertyType,
                offerType = listing.offerType?.value
            )
        }
    }
}
