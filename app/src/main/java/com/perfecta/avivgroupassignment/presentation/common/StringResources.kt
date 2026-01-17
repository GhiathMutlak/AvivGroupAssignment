package com.perfecta.avivgroupassignment.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import com.perfecta.avivgroupassignment.R
import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.model.OfferType

@Composable
fun OfferType.localizedName(): String = when (this) {
    OfferType.SALE -> stringResource(R.string.offer_type_sale)
    OfferType.RENT -> stringResource(R.string.offer_type_rent)
    OfferType.MORTGAGE -> stringResource(R.string.offer_type_mortgage)
}

@Composable
fun Listing.localizedPrice(): String {
    val formattedNumber = String.format(
        locale = Locale.current.platformLocale,
        format = "%,.0f",
        price ?: 0.0
    )
    return stringResource(R.string.currency_format, formattedNumber)
}

@Composable
fun Listing.localizedArea(): String {
    val formattedNumber = String.format(
        locale = Locale.current.platformLocale,
        format = "%.0f",
        area ?: 0.0
    )
    return stringResource(R.string.area_format, formattedNumber)
}
