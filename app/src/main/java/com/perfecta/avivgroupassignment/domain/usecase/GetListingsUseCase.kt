package com.perfecta.avivgroupassignment.domain.usecase

import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.repository.ListingRepository
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.Error
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListingsUseCase @Inject constructor(
    private val repository: ListingRepository
) {
    suspend operator fun invoke(): Flow<AvivResult<List<Listing>, Error>> {
        return repository.getListings()
    }
}
