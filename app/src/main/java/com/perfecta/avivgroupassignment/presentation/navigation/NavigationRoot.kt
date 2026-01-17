package com.perfecta.avivgroupassignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.perfecta.avivgroupassignment.presentation.details.DetailsContract
import com.perfecta.avivgroupassignment.presentation.details.DetailsScreen
import com.perfecta.avivgroupassignment.presentation.details.DetailsViewModel
import com.perfecta.avivgroupassignment.presentation.listings.ListingsContract
import com.perfecta.avivgroupassignment.presentation.listings.ListingsScreen
import com.perfecta.avivgroupassignment.presentation.listings.ListingsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Listings.route
    ) {
        composable(Listings.route) {
            val viewModel: ListingsViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.events.collectLatest { event ->
                    when (event) {
                        is ListingsContract.Event.NavigateToDetails -> {
                            navController.navigate(ListingDetails.createRoute(event.listingId))
                        }
                        is ListingsContract.Event.ShowError -> {
                            // TODO: Show error
                        }
                    }
                }
            }

            ListingsScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }

        composable(
            route = ListingDetails.route,
            arguments = ListingDetails.arguments
        ) { navBackStackEntry ->
            val viewModel: DetailsViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.events.collectLatest { event ->
                    when (event) {
                        DetailsContract.Event.NavigateBack -> navController.popBackStack()
                        is DetailsContract.Event.ShowError -> TODO()
                    }
                }
            }

            DetailsScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
    }
}