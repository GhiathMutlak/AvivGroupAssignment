package com.perfecta.avivgroupassignment.presentation

import app.cash.turbine.test
import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.model.OfferType
import com.perfecta.avivgroupassignment.domain.usecase.GetListingDetailsUseCase
import com.perfecta.avivgroupassignment.domain.usecase.GetListingsUseCase
import com.perfecta.avivgroupassignment.domain.usecase.RefreshListingsUseCase
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.DataError
import com.perfecta.avivgroupassignment.presentation.adaptiveListingScreen.AdaptiveListingsViewModel
import com.perfecta.avivgroupassignment.presentation.adaptiveListingScreen.AdaptiveScreenContract
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdaptiveListingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getListingsUseCase: GetListingsUseCase
    private lateinit var getListingDetailsUseCase: GetListingDetailsUseCase
    private lateinit var refreshListingsUseCase: RefreshListingsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getListingsUseCase = mockk()
        getListingDetailsUseCase = mockk()
        refreshListingsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load updates state with listings`() = runTest {
        val listings = listOf(
            createListing(1, "Berlin"),
            createListing(2, "Hamburg")
        )
        coEvery { getListingsUseCase() } returns flowOf(AvivResult.Success(listings))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(2, viewModel.state.value.listings.size)
        assertFalse(viewModel.state.value.isListingsLoading)
    }

    @Test
    fun `OnListingClicked sets selectedListingId and loads details`() = runTest {
        val listings = listOf(createListing(1, "Berlin"))
        val detailListing = createListing(1, "Berlin")
        coEvery { getListingsUseCase() } returns flowOf(AvivResult.Success(listings))
        coEvery { getListingDetailsUseCase(1) } returns flowOf(AvivResult.Success(detailListing))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(AdaptiveScreenContract.Action.OnListingClicked(1))
        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.selectedListingId)
        assertEquals("Berlin", viewModel.state.value.selectedListing?.city)
    }

    @Test
    fun `refresh failure emits ShowError event`() = runTest {
        coEvery { getListingsUseCase() } returns flowOf(AvivResult.Success(emptyList()))
        coEvery { refreshListingsUseCase() } returns AvivResult.Failure(
            DataError.Network.NO_INTERNET,
            cause = Exception("No connection")
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.events.test {
            viewModel.onAction(AdaptiveScreenContract.Action.RefreshListings)
            advanceUntilIdle()

            val event = awaitItem()
            assertEquals("No connection", (event as AdaptiveScreenContract.Event.ShowError).message)
            assertFalse(viewModel.state.value.isRefreshing)
        }
    }

    private fun createViewModel() = AdaptiveListingsViewModel(
        getListingsUseCase,
        getListingDetailsUseCase,
        refreshListingsUseCase
    )

    private fun createListing(id: Int, city: String) = Listing(
        id = id,
        city = city,
        price = 150000.0,
        area = 70.0,
        bedrooms = 2,
        rooms = 4,
        imageUrl = null,
        professional = "Test Agent",
        propertyType = "Apartment",
        offerType = OfferType.SALE
    )
}
