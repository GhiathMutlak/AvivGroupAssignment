package com.perfecta.avivgroupassignment.presentation.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ListingsViewModel @Inject constructor(
    private val getListingsUseCase: GetListingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListingsContract.State())
    val state: StateFlow<ListingsContract.State> = _state.asStateFlow()

    private val _eventsChannel = Channel<ListingsContract.Event>(Channel.BUFFERED)
    val events = _eventsChannel.receiveAsFlow()

    init {
        loadListings()
    }

    fun onAction(action: ListingsContract.Action) {
        when (action) {
            is ListingsContract.Action.OnListingClicked -> {
                viewModelScope.launch {
                    _eventsChannel.send(ListingsContract.Event.NavigateToDetails(action.listingId))
                }
            }
            is ListingsContract.Action.LoadListings -> {
                loadListings()
            }
            is ListingsContract.Action.RetryLoadListings -> {
                loadListings()
            }
        }
    }

    private fun loadListings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getListingsUseCase()
                .onSuccess { list ->
                    _state.update {
                        it.copy(
                            listings = list,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error, cause ->
                    val errorMessage = cause?.localizedMessage ?: "Unknown error occurred"
                    _state.update { it.copy(isLoading = false) }
                    _eventsChannel.send(ListingsContract.Event.ShowError(errorMessage))
                }
        }
    }
}
