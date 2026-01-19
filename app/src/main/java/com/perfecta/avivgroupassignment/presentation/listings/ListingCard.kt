package com.perfecta.avivgroupassignment.presentation.listings

import android.content.res.Configuration
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
fun ListingCard(
    listing: Listing,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                AsyncImage(
                    model = listing.imageUrl,
                    contentDescription = stringResource(R.string.content_desc_property_image),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_house_placeholder),
                    error = painterResource(R.drawable.ic_house_placeholder),
                    fallback = painterResource(R.drawable.ic_house_placeholder)
                )

                listing.offerType?.let { offerType ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = offerType.localizedName(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
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
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = listing.propertyType ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = listing.city ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                val notAvailable = stringResource(R.string.text_not_available)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PropertyDetail(
                        label = stringResource(R.string.label_area),
                        value = listing.localizedArea()
                    )
                    PropertyDetail(
                        label = stringResource(R.string.label_bedrooms),
                        value = listing.bedrooms?.toString() ?: notAvailable
                    )
                    PropertyDetail(
                        label = stringResource(R.string.label_rooms),
                        value = listing.rooms?.toString() ?: notAvailable
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyDetail(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
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

@Preview(showBackground = true)
@Composable
private fun ListingCardSalePreview() {
    AvivGroupAssignmentTheme {
        ListingCard(
            listing = previewListingSale,
            onClick = {}
        )
    }
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ListingCardRentPreview() {
    AvivGroupAssignmentTheme {
        ListingCard(
            listing = previewListingRent,
            onClick = {}
        )
    }
}
