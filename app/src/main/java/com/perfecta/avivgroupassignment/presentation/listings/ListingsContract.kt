package com.perfecta.avivgroupassignment.presentation.listings

import com.perfecta.avivgroupassignment.domain.model.Listing

object ListingsContract {
    data class State(
        val listings: List<Listing> = emptyList(),
        val isLoading: Boolean = false,
    )

    sealed interface Action {
        data object LoadListings : Action
        data object RetryLoadListings : Action
        data class OnListingClicked(val listingId: Int) : Action
    }

    sealed interface Event {
        data class NavigateToDetails(val listingId: Int) : Event
        data class ShowError(val message: String) : Event
    }
}
