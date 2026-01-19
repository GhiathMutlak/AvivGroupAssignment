package com.perfecta.avivgroupassignment.presentation.adaptiveListingScreen

import com.perfecta.avivgroupassignment.domain.model.Listing

object AdaptiveScreenContract {
    data class State(
        val listings: List<Listing> = emptyList(),
        val isListingsLoading: Boolean = false,
        val selectedListingId: Int? = null,
        val selectedListing: Listing? = null,
        val isDetailsLoading: Boolean = false,
        val isRefreshing: Boolean = false,
    )

    sealed interface Action {
        data object LoadListings: Action
        data object RetryLoadListings: Action
        data class OnListingClicked(val listingId: Int): Action
        data object ClearSelection: Action
        data object RefreshListings : Action
    }

    sealed interface Event {
        data class ShowError(val message: String): Event
    }
}