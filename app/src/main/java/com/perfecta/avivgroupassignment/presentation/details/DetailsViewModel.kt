package com.perfecta.avivgroupassignment.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perfecta.avivgroupassignment.domain.usecase.GetListingDetailsUseCase
import com.perfecta.avivgroupassignment.domain.util.onFailure
import com.perfecta.avivgroupassignment.domain.util.onSuccess
import com.perfecta.avivgroupassignment.presentation.navigation.ListingDetails.LISTING_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getListingDetailsUseCase: GetListingDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsContract.State())
    val state: StateFlow<DetailsContract.State> = _state.asStateFlow()

    private val _eventsChannel = Channel<DetailsContract.Event>(Channel.BUFFERED)
    val events = _eventsChannel.receiveAsFlow()

    private val listingId: Int = checkNotNull(savedStateHandle[LISTING_ID_ARG])

    init {
        onAction(DetailsContract.Action.LoadListingDetails(listingId))
    }

    fun onAction(action: DetailsContract.Action) {
        when (action) {
            is DetailsContract.Action.LoadListingDetails -> loadListingDetails(action.listingId)
            is DetailsContract.Action.RetryLoadDetails -> loadListingDetails(action.listingId)
            DetailsContract.Action.NavigateBack -> {
                viewModelScope.launch {
                    _eventsChannel.send(DetailsContract.Event.NavigateBack)
                }
            }
        }
    }

    private fun loadListingDetails(listingId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getListingDetailsUseCase(listingId)
                .onSuccess { listing ->
                    _state.update {
                        it.copy(
                            listing = listing,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error, cause ->
                    val errorMessage = cause?.localizedMessage ?: "Unknown error occurred"
                    _state.update { it.copy(isLoading = false) }
                    _eventsChannel.send(DetailsContract.Event.ShowError(errorMessage))
                }
        }
    }
}
