package com.perfecta.avivgroupassignment.domain.usecase

import com.perfecta.avivgroupassignment.domain.repository.ListingRepository
import com.perfecta.avivgroupassignment.domain.util.EmptyResult
import com.perfecta.avivgroupassignment.domain.util.Error
import javax.inject.Inject

class RefreshListingsUseCase @Inject constructor(
    private val repository: ListingRepository
) {
    suspend operator fun invoke(): EmptyResult<Error> {
        return repository.refreshListings()
    }
}