package com.perfecta.avivgroupassignment.presentation.adaptiveListingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perfecta.avivgroupassignment.domain.usecase.GetListingDetailsUseCase
import com.perfecta.avivgroupassignment.domain.usecase.GetListingsUseCase
import com.perfecta.avivgroupassignment.domain.util.onFailure
import com.perfecta.avivgroupassignment.domain.util.onSuccess
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
class AdaptiveListingsViewModel @Inject constructor(
    private val getListingsUseCase: GetListingsUseCase,
    private val getListingDetailsUseCase: GetListingDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AdaptiveScreenContract.State())
    val state: StateFlow<AdaptiveScreenContract.State> = _state.asStateFlow()

    private val _eventsChannel = Channel<AdaptiveScreenContract.Event>(Channel.BUFFERED)
    val events = _eventsChannel.receiveAsFlow()

    init {
        loadListings()
    }

    fun onAction(action: AdaptiveScreenContract.Action) {
        when (action) {
            is AdaptiveScreenContract.Action.LoadListings -> loadListings()
            is AdaptiveScreenContract.Action.RetryLoadListings -> loadListings()
            is AdaptiveScreenContract.Action.OnListingClicked -> {
                _state.update { it.copy(selectedListingId = action.listingId) }
                loadListingDetails(action.listingId)
            }
            is AdaptiveScreenContract.Action.ClearSelection -> {
                _state.update { it.copy(selectedListingId = null, selectedListing = null) }
            }
        }
    }

    private fun loadListings() {
        viewModelScope.launch {
            _state.update { it.copy(isListingsLoading = true) }

            getListingsUseCase()
                .onSuccess { list ->
                    _state.update {
                        it.copy(
                            listings = list,
                            isListingsLoading = false,
                        )
                    }
                }
                .onFailure { error, cause ->
                    val errorMessage = cause?.localizedMessage ?: "Unknown error occurred"
                    _state.update { it.copy(isListingsLoading = false) }
                    _eventsChannel.send(AdaptiveScreenContract.Event.ShowError(errorMessage))
                }
        }
    }

    private fun loadListingDetails(listingId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isDetailsLoading = true) }

            getListingDetailsUseCase(listingId)
                .onSuccess { listing ->
                    _state.update {
                        it.copy(
                            selectedListing = listing,
                            isDetailsLoading = false,
                        )
                    }
                }
                .onFailure { error, cause ->
                    val errorMessage = cause?.localizedMessage ?: "Unknown error occurred"
                    _state.update { it.copy(isDetailsLoading = false) }
                    _eventsChannel.send(AdaptiveScreenContract.Event.ShowError(errorMessage))
                }
        }
    }
}