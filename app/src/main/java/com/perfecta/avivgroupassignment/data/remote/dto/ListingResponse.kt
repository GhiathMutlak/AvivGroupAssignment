package com.perfecta.avivgroupassignment.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListingsResponse(
    @SerialName("items")
    val items: List<ListingDto>,
    @SerialName("totalCount")
    val totalCount: Int?
)
