package com.perfecta.avivgroupassignment.presentation.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AdaptiveScreenContract.Event.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        AdaptiveListingsScreen(
            state = state,
            onAction = viewModel::onAction
        )
    }
}