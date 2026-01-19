package com.perfecta.avivgroupassignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.perfecta.avivgroupassignment.presentation.adaptiveListingScreen.AdaptiveListingsScreen
import com.perfecta.avivgroupassignment.presentation.adaptiveListingScreen.AdaptiveListingsViewModel
import com.perfecta.avivgroupassignment.presentation.adaptiveListingScreen.AdaptiveScreenContract
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NavigationRoot() {
    val viewModel: AdaptiveListingsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AdaptiveScreenContract.Event.ShowError -> {
                    // TODO: Show error
                }
            }
        }
    }

    AdaptiveListingsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}