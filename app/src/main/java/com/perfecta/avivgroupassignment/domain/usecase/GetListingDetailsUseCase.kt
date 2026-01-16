package com.perfecta.avivgroupassignment.domain.usecase

import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.repository.ListingRepository
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.Error
import javax.inject.Inject

class GetListingDetailsUseCase @Inject constructor(
    private val repository: ListingRepository
) {
    suspend operator fun invoke(listingId: Int): AvivResult<Listing, Error> {
        return repository.getListingDetails(listingId)
    }
}
