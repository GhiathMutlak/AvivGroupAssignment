package com.perfecta.avivgroupassignment.presentation.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.perfecta.avivgroupassignment.R
import com.perfecta.avivgroupassignment.domain.model.Listing
import com.perfecta.avivgroupassignment.domain.model.OfferType
import com.perfecta.avivgroupassignment.ui.theme.AvivGroupAssignmentTheme
import com.perfecta.avivgroupassignment.presentation.common.localizedArea
import com.perfecta.avivgroupassignment.presentation.common.localizedName
import com.perfecta.avivgroupassignment.presentation.common.localizedPrice

@Composable
fun ListingDetailsContent(
    listing: Listing,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
        ) {
            AsyncImage(
                model = listing.imageUrl,
                contentDescription = stringResource(R.string.content_desc_property_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_house_placeholder),
                error = painterResource(R.drawable.ic_house_placeholder),
                fallback = painterResource(R.drawable.ic_house_placeholder)
            )

            listing.offerType?.let { offerType ->
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = offerType.localizedName(),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = listing.localizedPrice(),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            listing.propertyType?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            listing.city?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.property_details),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                val notAvailable = stringResource(R.string.text_not_available)
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailRow(
                        label = stringResource(R.string.label_area),
                        value = listing.localizedArea()
                    )
                    HorizontalDivider()
                    DetailRow(
                        label = stringResource(R.string.label_bedrooms),
                        value = listing.bedrooms?.toString() ?: notAvailable
                    )
                    HorizontalDivider()
                    DetailRow(
                        label = stringResource(R.string.label_rooms),
                        value = listing.rooms?.toString() ?: notAvailable
                    )

                    listing.professional?.let {
                        HorizontalDivider()
                        DetailRow(
                            label = stringResource(R.string.label_professional),
                            value = it
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}


private val previewListingSale = Listing(
    id = 1,
    city = "Paris",
    price = 450000.0,
    area = 85.0,
    bedrooms = 2,
    rooms = 4,
    imageUrl = null,
    professional = "GSL CONTACTING",
    propertyType = "Apartment",
    offerType = OfferType.SALE
)

private val previewListingRent = Listing(
    id = 2,
    city = "Lyon",
    price = 1200.0,
    area = 45.0,
    bedrooms = 1,
    rooms = 2,
    imageUrl = null,
    professional = "GSL CONTACTING",
    propertyType = "Studio",
    offerType = OfferType.RENT
)

@Preview(showBackground = true, widthDp = 400, heightDp = 700)
@Composable
private fun ListingDetailsContentSalePreview() {
    AvivGroupAssignmentTheme {
        ListingDetailsContent(listing = previewListingSale)
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 700,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ListingDetailsContentRentPreview() {
    AvivGroupAssignmentTheme {
        ListingDetailsContent(listing = previewListingRent)
    }
}