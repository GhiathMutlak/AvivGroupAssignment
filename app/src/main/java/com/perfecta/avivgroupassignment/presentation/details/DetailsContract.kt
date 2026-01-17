package com.perfecta.avivgroupassignment.presentation.details

import com.perfecta.avivgroupassignment.domain.model.Listing

object DetailsContract {
    data class State(
        val listing: Listing? = null,
        val isLoading: Boolean = false,
    )

    sealed interface Action {
        data class LoadListingDetails(val listingId: Int): Action
        data class RetryLoadDetails(val listingId: Int): Action
        data object NavigateBack: Action
    }

    sealed interface Event {
        data object NavigateBack: Event
        data class ShowError(val message: String): Event
    }
}
