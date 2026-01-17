package com.perfecta.avivgroupassignment.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Screen {
    val route: String
}

data object Listings : Screen {
    override val route = "listings"
}

data object ListingDetails : Screen {
    const val LISTING_ID_ARG = "listingId"

    override val route = "listing/{$LISTING_ID_ARG}"

    val arguments = listOf(
        navArgument(LISTING_ID_ARG) { type = NavType.IntType }
    )

    fun createRoute(listingId: Int) = "listing/$listingId"
}
