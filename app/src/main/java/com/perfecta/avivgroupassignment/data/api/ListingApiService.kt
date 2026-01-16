package com.perfecta.avivgroupassignment.data.api

import com.perfecta.avivgroupassignment.data.dto.ListingDto
import com.perfecta.avivgroupassignment.data.dto.ListingsResponse
import com.perfecta.avivgroupassignment.data.networking.get
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.DataError
import io.ktor.client.HttpClient


class ListingApiService(private val client: HttpClient) {

    suspend fun getListings(): AvivResult<ListingsResponse, DataError> =
        client.get<ListingsResponse>("listings.json")

    suspend fun getListingDetails(listingId: Int): AvivResult<ListingDto, DataError> =
        client.get<ListingDto>("listings/$listingId.json")
}