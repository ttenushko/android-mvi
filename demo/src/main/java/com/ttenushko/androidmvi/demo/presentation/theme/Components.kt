package com.ttenushko.androidmvi.demo.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ttenushko.androidmvi.demo.domain.weather.model.Place

@Composable
fun SemitransparentProgress() {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f))
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun SmallProgress(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .size(16.dp, 16.dp),
        strokeWidth = 1.dp
    )
}

@Composable
fun PlaceCards(
    modifier: Modifier,
    places: List<Place>,
    placeClickHandler: (Place) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(
            items = places,
            key = { _, place -> place.id }
        ) { index, place ->
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (0 == index) 8.dp else 16.dp)
            )
            PlaceCard(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                place,
                placeClickHandler
            )
            if (index == places.size - 1) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
            }
        }
    }
}

@Composable
fun PlaceCard(
    modifier: Modifier,
    place: Place,
    placeClickHandler: (Place) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 8.dp,
        modifier = modifier
            .clickable(onClick = { placeClickHandler(place) })
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "${place.name}, ${place.countyCode}",
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "%1\$.6f, %2\$.6f".format(place.location.latitude, place.location.longitude),
                style = MaterialTheme.typography.body2
            )
        }
    }
}